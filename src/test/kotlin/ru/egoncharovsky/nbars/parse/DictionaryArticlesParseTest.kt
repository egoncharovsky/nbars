package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.article.DictionaryArticle
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.article.ReferenceArticle
import ru.egoncharovsky.nbars.getResource
import kotlin.test.assertEquals

class DictionaryArticlesParseTest {

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun parameters() = listOf(
            Articles.adjutant,
            Articles.tarnish,
            Articles.someone,
            Articles.swagman,
            Articles.arry,
            ExpressionArticles.tie_in,
            ReferenceArticles.ible,
            ReferenceArticles.peet,
            ReferenceArticles.pence
        ).map { arrayOf(it.headword, it) }
    }

    @ParameterizedTest()
    @MethodSource("parameters")
    fun parse(key: String, expected: DictionaryArticle) {
        val path = when (expected) {
            is Article -> "card/$key.dsl"
            is ExpressionArticle -> "card/expressions/${key.replace(" ", "_")}.dsl"
            is ReferenceArticle -> "card/references/$key.dsl"
            else -> throw IllegalArgumentException("Unknown type $expected")
        }

        val lines = getResource(path).readLines()

        val parser = DictionaryParser()

        val article = parser.parse(key, lines)
        assertEquals(expected, article)
    }
}