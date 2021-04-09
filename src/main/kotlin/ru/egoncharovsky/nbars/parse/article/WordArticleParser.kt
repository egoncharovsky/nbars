package ru.egoncharovsky.nbars.parse.article

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.compound
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.WordArticle
import ru.egoncharovsky.nbars.entity.article.section.SpecializedVocabulary
import ru.egoncharovsky.nbars.entity.article.section.WordArticleSection
import ru.egoncharovsky.nbars.entity.article.section.WordHomonym
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.StepParseException
import ru.egoncharovsky.nbars.parse.RawPart
import ru.egoncharovsky.nbars.parse.ReferenceToArticleParser
import ru.egoncharovsky.nbars.parse.TextParser
import ru.egoncharovsky.nbars.parse.TranslationParser

class WordArticleParser : ArticleParser<WordArticleSection>() {

    override val logger = KotlinLogging.logger { }

    private val textParser = TextParser()
    private val translationParser = TranslationParser()
    private val referenceToArticleParser = ReferenceToArticleParser()

    fun parse(headword: String, raw: RawPart): WordArticle {
        logger.trace("Parsing article $headword from $raw")

        val compound = raw.findPart(compound)?.let { textParser.parse(it) }

        return WordArticle(headword, parseBody(raw), compound)
    }

    override fun parseSection(raw: RawPart): WordArticleSection {
        return when {
            raw.contains(partOfSpeech) && raw.contains(translation) -> parseLexicalGrammaticalHomonym(raw)
            !raw.contains(translation) && raw.contains(reference) -> referenceToArticleParser.parse(raw)
            raw.contains(translation) || raw.contains(example) || raw.contains(comment) -> {
                raw.removeAll(plain)
                SpecializedVocabulary("")
            }
            else -> throw StepParseException(
                "section",
                "raw doesn't any of: " +
                        "$partOfSpeech with $translation " +
                        "| $reference " +
                        "| $translation or $example or $comment",
                raw
            )
        }
    }

    private fun parseLexicalGrammaticalHomonym(raw: RawPart): WordHomonym {
        logger.trace("Parse lex. gram. homonym from: $raw")

        raw
            .removeAll(boldTag)
            .removeAll(colorTag) // todo parse semantic sections

        val split = raw.split(translationMarker)
        val prefix = split[0]

        logger.trace("Lex. gram. homonym prefix: $prefix")

        prefix.removeAll(escapedSquareBrackets)
        val comment = prefix.findPartBefore(comment, translation)?.let {
            textParser.parse(it)
        }
        val transcription = textParser.parse(prefix.getPart(transcription, 0)) as Transcription
        val partOfSpeech = parsePartOfSpeech(prefix.getAll(partOfSpeech))

        val rawTranslations = split.drop(1)

        val translations = if (rawTranslations.isEmpty()) {
            listOf(translationParser.parse(prefix))
        } else {
            rawTranslations.map { translationParser.parse(it) }
        }
        val remark = prefix.findPart(plain)?.let { textParser.parse(it) } //todo check

        prefix.finishAll()

        return WordHomonym(transcription, partOfSpeech, translations, remark, comment)
    }

    internal fun parsePartOfSpeech(labels: List<String>): PartOfSpeech {
        return when (labels.size) {
            1 -> PartOfSpeech.byLabel(labels[0])
            2 -> PartOfSpeech.byLabel(labels[1], labels[0])
            else -> throw IllegalArgumentException("Can't parse part of speech from $labels: unexpected size ${labels.size}")
        }
    }
}