package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.getResource
import kotlin.test.assertEquals

class ArticlesParseTest {

    companion object {
        @JvmStatic
        fun parameters() = listOf(
//            Articles.adjutant,
//            Articles.tarnish,
//            Articles.someone,
            Articles.swagman
        ).map { arrayOf(it.headword, it) }
    }

    @ParameterizedTest()
    @MethodSource("parameters")
    fun parse(key: String, expected: Article) {
        val lines = getResource("card/$key.dsl").readLines()
        val parser = ArticleParser()

        val article = parser.parse(key, lines)
        assertEquals(expected, article)
    }
}