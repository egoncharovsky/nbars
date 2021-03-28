package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.entity.*
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.doubleBraces
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.italicTag
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lexicalGrammarHomonymMarker
import ru.egoncharovsky.nbars.Regexes.marginTag
import ru.egoncharovsky.nbars.Regexes.mirroredSquareBrackets
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.Regexes.translationVariantMarker
import ru.egoncharovsky.nbars.entity.Translation.Variant

class ArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

    fun parse(key: String, bodyLines: List<String>): Article {
        val body = bodyLines.joinToString("") { it.trim() }
            .replace(marginTag, "")
            .replace(boldTag, "")
            .replace(italicTag, "")
            .replace(colorTag, "")
            .replace(doubleBraces, "")
            .replace(mirroredSquareBrackets, "")

        val raw = RawPart(body)
        val homonyms = parseBody(raw)

        raw.finishAll()

        return Article(key, homonyms)
    }

    internal fun parseBody(raw: RawPart): List<List<Homonym>> {
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
        prefix.finish()

        return homonyms
    }

    internal fun parseHomonym(raw: RawPart): List<Homonym> {
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
        prefix.finish()

        return lexGramHomonyms
    }

    internal fun parseLexicalGrammaticalHomonym(raw: RawPart): Homonym {
        logger.trace("Parse lex. gram. homonym from: $raw")

        val split = raw.split(translationMarker)
        val prefix = split[0]

        logger.trace("Lex. gram. homonym prefix: $prefix")

        val transcription = prefix.get(transcription)
        val partOfSpeech = prefix.get(partOfSpeech)

        val rawTranslations = split.drop(1)

        val translations = if (rawTranslations.isEmpty()) {
            listOf(parseTranslation(prefix))
        } else {
            rawTranslations.map { parseTranslation(it) }
        }
        prefix.finish()

        return Homonym(transcription, partOfSpeech, translations)
    }

    internal fun parseTranslation(raw: RawPart): Translation {
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
            prefix.finish()
        }
    }

    internal fun parseVariant(raw: RawPart): Variant {
        logger.trace("Parse translation variant from: $raw")

        val meaning = textParser.parse(raw.getPart(translation))
        val comment = raw.findPart(comment)?.let { textParser.parse(it) }
        val remark = raw.findPart(label, 0)?.let { textParser.parse(it) }
        raw.finish()

        return Variant(meaning, remark, comment)
    }
}