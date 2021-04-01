package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.Homonym
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.ParseException

class ArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

    fun parse(headword: String, raw: RawPart): Article {
        logger.trace("Parsing article $headword from $raw")

        val homonyms = parseBody(raw)
        raw.finishAll()

        return Article(headword, homonyms)
    }


    private fun parseBody(raw: RawPart): List<List<Homonym>> {
        logger.trace("Parse body from: $raw")

        val split = raw.split(Regexes.homonymMarker)
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

        val split = raw.split(Regexes.lexicalGrammarHomonymMarker)
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

        val split = raw.split(Regexes.translationMarker)
        val prefix = split[0]

        logger.trace("Lex. gram. homonym prefix: $prefix")

        prefix.removeAll(Regexes.escapedSquareBrackets)
        val comment = prefix.findPartBefore(Regexes.comment, Regexes.translation)?.let {
            textParser.parse(it)
        }
        val transcription = textParser.parse(prefix.getPart(Regexes.transcription, 0)) as Transcription
        val partOfSpeech = parsePartOfSpeech(prefix.getAll(Regexes.partOfSpeech))

        val rawTranslations = split.drop(1)

        val translations = if (rawTranslations.isEmpty()) {
            listOf(parseTranslation(prefix))
        } else {
            rawTranslations.map { parseTranslation(it) }
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

    private fun parseTranslation(raw: RawPart): Translation {
        logger.trace("Parse translation from: $raw")

        val split = raw.split(Regexes.translationVariantMarker)
        val prefix: RawPart = split[0]

        logger.trace("Translation prefix: $prefix")

        val rawVariants = split.drop(1)

        return if (rawVariants.isEmpty()) {
            Translation(listOf(parseVariant(prefix)))
        } else {
            Translation(
                rawVariants.map { parseVariant(it) },
                comment = prefix.findPart(Regexes.comment)?.let { textParser.parse(it) },
                remark = prefix.findPart(Regexes.label, 0)?.let { textParser.parse(it) }
            )
        }.also {
            prefix.finishAll()
        }
    }

    internal fun parseVariant(raw: RawPart): Translation.Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(Regexes.example).map { parseExample(it) }

        return when {
            raw.contains(Regexes.translation) -> {
                val meaning = textParser.parse(raw.getPart(Regexes.translation))

                val comment = raw.findPart(Regexes.comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(Regexes.label, 0)?.let { textParser.parse(it) }

                Translation.Variant(meaning, remark, comment, examples)
            }
            raw.contains(Regexes.reference) -> {
                val comment = raw.findPart(Regexes.comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(Regexes.label, 0)?.let { textParser.parse(it) }

                val reference = textParser.parse(raw)

                Translation.Variant(reference, remark, comment, examples)
            }
            else -> throw ParseException(
                "meaning",
                "no translation by '${Regexes.translation}' or reference by '${Regexes.reference}' found",
                raw
            )
        }.also {
            raw.finishAll()
        }
    }

    private fun parseExample(raw: RawPart): Example {

        val text = textParser.parse(raw.getPart(Regexes.lang, 0)) as ForeignText
        val translation = textParser.parse(raw.remove(Regexes.dash))

        return Example(text, translation)
    }
}