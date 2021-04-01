package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.doubleBraces
import ru.egoncharovsky.nbars.Regexes.italicTag
import ru.egoncharovsky.nbars.Regexes.marginTag
import ru.egoncharovsky.nbars.Regexes.optionalTag
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.entity.article.DictionaryArticle

class DictionaryParser {

    private val logger = KotlinLogging.logger { }
    private val articleParser = ArticleParser()
    private val expressionArticleParser = ExpressionArticleParser()
    private val referenceArticleParser = ReferenceArticleParser()

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
            !body.contains(partOfSpeech) && headword.trim().contains(" ") -> expressionArticleParser.parse(headword, raw)
            !body.contains(partOfSpeech) && body.contains(reference) -> referenceArticleParser.parse(headword, raw)
            else -> articleParser.parse(headword, raw)
        }
    }
}