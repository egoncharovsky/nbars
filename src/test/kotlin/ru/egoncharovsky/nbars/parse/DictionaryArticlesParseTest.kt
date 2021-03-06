package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.WordArticle
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
            Articles.peet,
            Articles.pence,
            Articles.narrator,
            Articles.a_la,
            Articles.abatement,
            Articles.excepting,
            Articles.ll,
            Morphemes.ade,
            Morphemes.ad,
            Morphemes.ible,
            Articles.abed,
            Articles.abeyance,
            ExpressionArticles.a_posteriori,
            Articles.tinct,
            Articles.d,
            Articles.cosec,
            Articles.prosequence,
            Articles.oatmeal,
            Articles.seamy,
            ExpressionArticles.blue_murder,
            Articles.albert,
            Articles.virtuosa,
            Articles.scoring,
            ExpressionArticles.ad_libitum,
            Articles.angloAmerican
        ).map { arrayOf(it.headword, it) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun parse(key: String, expected: Article<*>) {
        val path = key.replace(" ", "_").let {
            when (expected) {
                is WordArticle -> "card/$it.dsl"
                is ExpressionArticle -> "card/expressions/$it.dsl"
                is MorphemeArticle -> "card/morphemes/$it.dsl"
                else -> throw IllegalArgumentException("Unknown type $expected")
            }
        }

        val lines = getResource(path).readLines()

        val parser = DictionaryParser()

        val article = parser.parse(key, lines)
        assertEquals(expected, article)
    }

    //    @Test
    fun test() {
        parse("'Fro", Articles.excepting)
    }
}