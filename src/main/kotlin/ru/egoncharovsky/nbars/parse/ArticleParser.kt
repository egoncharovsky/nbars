package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.entity.*

class ArticleParser {

    private val logger = KotlinLogging.logger { }

    private val marginTag = "\\[/?m\\d?]".toRegex()
    private val boldTag = "\\[/?b]".toRegex()
    private val italicTag = "\\[/?i]".toRegex()
    private val colorTag = "\\[/?c ?.*?]".toRegex()
    private val braces = "\\{\\{|}}".toRegex()

    private val homonymMarker = "\\[sup]\\d+?\\[/sup]".toRegex()
    private val lexicalGrammarHomonymMarker = "[Ⅰ-Ⅹ]".toRegex()
    private val translationMarker = "\\d+?\\.".toRegex()
    private val translationVariantMarker = "\\d+?\\)".toRegex()

    private val transcription = "\\\\[\\[t](.+?)\\[/t]\\\\]".toRegex()
    private val translation = "\\[trn](.+?)\\[/trn]".toRegex()
    private val label = "\\[p](.+?)\\[/p]".toRegex()

    private val partOfSpeech = "\\[p](n|a)\\[/p]".toRegex()

    fun parse(key: String, bodyLines: List<String>): Article {
        val body = bodyLines.joinToString("") { it.trim() }
            .replace(marginTag, "")
            .replace(boldTag, "")
            .replace(italicTag, "")
            .replace(colorTag, "")
            .replace(braces, "")

        val raw = RawPart(body)
        val homonyms = parseBody(raw)

        raw.finishAll()

        return Article(key, homonyms)
    }

    private fun parseBody(raw: RawPart): List<Homonym> {
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

    private fun parseHomonym(raw: RawPart): Homonym {
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

        return Homonym(lexGramHomonyms)
    }

    private fun parseLexicalGrammaticalHomonym(raw: RawPart): LexGramHomonym {
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

        return LexGramHomonym(transcription, partOfSpeech, translations)
    }

    private fun parseTranslation(raw: RawPart): Translation {
        logger.trace("Parse translation from: $raw")

        val split = raw.split(translationVariantMarker)

        val rawVariants = split.drop(1)

        val variants: List<TranslationVariant>
        val prefix: RawPart

        if (rawVariants.isEmpty()) {
            val (before, rawVariant) = split[0].before(translation)
            prefix = before

            variants = listOf(parseVariant(rawVariant!!))
        } else {
            prefix = split[0]
            variants = rawVariants.map { parseVariant(it) }
        }

        logger.trace("Translation prefix: $prefix")
        val remark = prefix.find(label)

        prefix.finish()

        return Translation(variants, remark)
    }

    private fun parseVariant(raw: RawPart): TranslationVariant {
        logger.trace("Parse translation variant from: $raw")

        val meaning = raw.get(translation)
        val remark = raw.all()
        raw.finish()

        return TranslationVariant(meaning, remark)
    }
}