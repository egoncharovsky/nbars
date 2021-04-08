package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.doubleBraces
import ru.egoncharovsky.nbars.Regexes.italicTag
import ru.egoncharovsky.nbars.Regexes.marginTag
import ru.egoncharovsky.nbars.Regexes.optionalTag
import ru.egoncharovsky.nbars.Regexes.partOfSpeech
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.parse.article.ExpressionArticleParser
import ru.egoncharovsky.nbars.parse.article.MorphemeArticleParser
import ru.egoncharovsky.nbars.parse.article.WordArticleParser

class DictionaryParser {

    private val logger = KotlinLogging.logger { }
    private val articleParser = WordArticleParser()
    private val expressionArticleParser = ExpressionArticleParser()
    private val morphemeArticleParser = MorphemeArticleParser()

    fun parse(headword: String, bodyLines: List<String>): Article<*> {
        logger.debug("Parsing $headword with ${bodyLines.size} lines")

        val body = bodyLines.joinToString(" ") { it.trim() }

        val raw = RawPart(body)
            .removeAll(marginTag)
            .removeAll(italicTag)
            .removeAll(doubleBraces)
            .removeAll(optionalTag)

        return when {
            !body.contains(partOfSpeech) && headword.contains(" ") -> expressionArticleParser.parse(headword, raw)
            !body.contains(partOfSpeech) && (
                    headword.startsWith("-") || headword.endsWith("-") || headword.endsWith("'")
                    ) -> morphemeArticleParser.parse(headword, raw)
            else -> articleParser.parse(headword, raw)
        }
    }
}