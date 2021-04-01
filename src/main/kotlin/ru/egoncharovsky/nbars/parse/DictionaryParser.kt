package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.dash
import ru.egoncharovsky.nbars.Regexes.doubleBraces
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
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.article.DictionaryArticle
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.ParseException

class DictionaryParser {

    private val logger = KotlinLogging.logger { }
    private val articleParser = ArticleParser()
    private val expressionArticleParser = ExpressionArticleParser()

    fun parse(headword: String, bodyLines: List<String>): DictionaryArticle {
        logger.debug("Parsing $headword with ${bodyLines.size} lines")

        val body = bodyLines.joinToString("") { it.trim() }
            .replace(marginTag, "")
            .replace(boldTag, "")
            .replace(italicTag, "")
            .replace(colorTag, "")
            .replace(doubleBraces, "")
            .replace(optionalTag, "")

        val raw = RawPart(body)

        return when {
            headword.trim().contains(" ") -> expressionArticleParser.parse(headword, raw)
            else -> articleParser.parse(headword, raw)
        }
    }
}