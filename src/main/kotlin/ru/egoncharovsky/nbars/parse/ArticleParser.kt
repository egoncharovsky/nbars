package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.dash
import ru.egoncharovsky.nbars.Regexes.doubleBraces
import ru.egoncharovsky.nbars.Regexes.equal
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.italicTag
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.lexicalGrammarHomonymMarker
import ru.egoncharovsky.nbars.Regexes.marginTag
import ru.egoncharovsky.nbars.Regexes.optionalTag
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.Regexes.translationVariantMarker
import ru.egoncharovsky.nbars.entity.*
import ru.egoncharovsky.nbars.entity.Translation.Variant
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Transcription

class ArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

    fun parse(headword: String, bodyLines: List<String>): Article {
        logger.debug("Parsing $headword with ${bodyLines.size} lines")

        val body = bodyLines.joinToString("") { it.trim() }
            .replace(marginTag, "")
            .replace(boldTag, "")
            .replace(italicTag, "")
            .replace(colorTag, "")
            .replace(doubleBraces, "")
            .replace(optionalTag, "")

        val raw = RawPart(body)
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

        val split = raw.split(translationVariantMarker)
        val prefix: RawPart = split[0]

        logger.trace("Translation prefix: $prefix")

        val rawVariants = split.drop(1)

        return if (rawVariants.isEmpty()) {
            Translation(listOf(parseVariant(prefix)))
        } else {
            Translation(
                rawVariants.map { parseVariant(it) },
                comment = prefix.findPart(comment)?.let { textParser.parse(it) },
                remark = prefix.findPart(label, 0)?.let { textParser.parse(it) }
            )
        }.also {
            prefix.finishAll()
        }
    }

    internal fun parseVariant(raw: RawPart): Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(example).map { parseExample(it) }

        val meaning = (raw.findPart(translation) ?: raw.getPart(reference, 0).also {
            raw.remove(equal)
        }).let { textParser.parse(it) }
        val comment = raw.findPart(comment)?.let { textParser.parse(it) }
        val remark = raw.findPart(label, 0)?.let { textParser.parse(it) }

        raw.finishAll()

        return Variant(meaning, remark, comment, examples)
    }

    private fun parseExample(raw: RawPart): Example {

        val text = textParser.parse(raw.getPart(lang, 0)) as ForeignText
        val translation = textParser.parse(raw.remove(dash))

        return Example(text, translation)
    }
}