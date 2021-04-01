package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.lexicalGrammarHomonymMarker
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.Homonym
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.text.Transcription

class ArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val translationParser = TranslationParser()

    fun parse(headword: String, raw: RawPart): Article {
        logger.trace("Parsing article $headword from $raw")

        val homonyms = parseBody(raw)
        raw.finishAll()

        return Article(headword, homonyms)
    }


    private fun parseBody(raw: RawPart): List<List<Homonym>> {
        logger.trace("Parse body from: $raw")

        val split = raw.split(homonymMarker)
        val prefix = split[0]

        logger.trace("Body prefix: $prefix")

        val rawHomonyms = split.drop(1)

        val homonyms = if (rawHomonyms.isEmpty()) {
            listOf(parseHomonym(prefix))
        } else {
            rawHomonyms.map { parseHomonym(it) }
        }
        prefix.finishAll()

        return homonyms
    }

    private fun parseHomonym(raw: RawPart): List<Homonym> {
        logger.trace("Parse homonym from: $raw")

        val split = raw.split(lexicalGrammarHomonymMarker)
        val prefix = split[0]

        logger.trace("Homonym prefix: $prefix")

        val rawLexGramHomonyms = split.drop(1)

        val lexGramHomonyms = if (rawLexGramHomonyms.isEmpty()) {
            listOf(parseLexicalGrammaticalHomonym(prefix))
        } else {
            rawLexGramHomonyms.map { parseLexicalGrammaticalHomonym(it) }
        }
        prefix.finishAll()

        return lexGramHomonyms
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
        prefix.finishAll()

        return Homonym(transcription, partOfSpeech, comment, translations)
    }

    internal fun parsePartOfSpeech(labels: List<String>): PartOfSpeech {
        return when (labels.size) {
            1 -> PartOfSpeech.byLabel(labels[0])
            2 -> PartOfSpeech.byLabel(labels[1], labels[0])
            else -> throw IllegalArgumentException("Can't parse part of speech from $labels: unexpected size ${labels.size}")
        }
    }
}