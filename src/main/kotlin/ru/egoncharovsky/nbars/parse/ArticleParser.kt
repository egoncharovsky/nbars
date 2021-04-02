package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.grammaticalForm
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.lexicalGrammarHomonymMarker
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.superscriptTag
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.article.Homonym
import ru.egoncharovsky.nbars.entity.article.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.StepParseException

class ArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val translationParser = TranslationParser()
    private val exampleParser = ExampleParser()

    fun parse(headword: String, raw: RawPart): Article {
        logger.trace("Parsing article $headword from $raw")

        val homonyms = parseBody(raw)
        raw.finishAll()

        return Article(headword, homonyms)
    }


    private fun parseBody(raw: RawPart): List<List<Either<Homonym, ReferenceToArticle>>> {
        logger.trace("Parse body from: $raw")

        val split = raw.split(homonymMarker).onEach {
            it.removeAll(boldTag).removeAll(superscriptTag)
        }
        val prefix = split[0]

        logger.trace("Body prefix: $prefix")

        val rawHomonyms = split.drop(1)

        val homonyms = if (rawHomonyms.isEmpty()) {
            listOf(parseHomonyms(prefix))
        } else {
            rawHomonyms.map { parseHomonyms(it) }
        }
        prefix.finishAll()

        return homonyms
    }

    private fun parseHomonyms(raw: RawPart): List<Either<Homonym, ReferenceToArticle>> {
        logger.trace("Parse lex. gram. homonyms from: $raw")

        val split = raw.split(lexicalGrammarHomonymMarker)
        val prefix = split[0]

        logger.trace("Homonyms prefix: $prefix")

        val rawLexGramHomonyms = split.drop(1)

        val lexGramHomonyms = if (rawLexGramHomonyms.isEmpty()) {
            listOf(parseHomonymOrReference(prefix))
        } else {
            rawLexGramHomonyms.map { parseHomonymOrReference(it) }
        }
        prefix.finishAll()

        return lexGramHomonyms
    }

    private fun parseHomonymOrReference(raw: RawPart): Either<Homonym, ReferenceToArticle> {
        return when {
            raw.contains(partOfSpeech) && raw.contains(translation) -> Either.Left(parseLexicalGrammaticalHomonym(raw))
            raw.contains(reference) -> Either.Right(parseReferenceToArticle(raw))
            else -> throw StepParseException(
                "homonyms or reference",
                "raw doesn't contains both: $partOfSpeech and $reference",
                raw
            )
        }
    }

    private fun parseReferenceToArticle(raw: RawPart): ReferenceToArticle {
        logger.trace("Parse reference to article from: '$raw'")

        val examples = raw.findAllParts(Regexes.example).map { exampleParser.parse(it) }

        raw.removeAll(escapedSquareBrackets)

        val transcription = textParser.parse(raw.getPart(transcription, 0)) as Transcription
        val grammaticalForm = raw.find(grammaticalForm)?.let { GrammaticalForm.byLabel(it) }
        val toHeadword = textParser.parse(raw.getPart(plain, 0))

        return ReferenceToArticle(transcription, grammaticalForm, toHeadword, examples)
    }

    private fun parseLexicalGrammaticalHomonym(raw: RawPart): Homonym {
        logger.trace("Parse lex. gram. homonym from: $raw")

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
        val remark = prefix.findPart(plain)?.let { textParser.parse(it) }

        prefix.finishAll()

        return Homonym(transcription, partOfSpeech, remark, comment, translations)
    }

    internal fun parsePartOfSpeech(labels: List<String>): PartOfSpeech {
        return when (labels.size) {
            1 -> PartOfSpeech.byLabel(labels[0])
            2 -> PartOfSpeech.byLabel(labels[1], labels[0])
            else -> throw IllegalArgumentException("Can't parse part of speech from $labels: unexpected size ${labels.size}")
        }
    }
}