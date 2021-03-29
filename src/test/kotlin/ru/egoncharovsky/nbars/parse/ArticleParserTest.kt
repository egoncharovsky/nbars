package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.ArticleBuilder
import ru.egoncharovsky.nbars.VariantBuilder
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.st
import ru.egoncharovsky.nbars.readResourceLines
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ArticleParserTest {

    companion object {
        @JvmStatic
        fun parameters() = listOf(
            Articles.adjutant,
            Articles.tarnish
        ).map { arrayOf(it.headword, it) }
    }

    @ParameterizedTest()
    @MethodSource("parameters")
    fun parse(key: String, expected: Article) {
        val lines = readResourceLines("card/$key.dsl")
        val parser = ArticleParser()

        val article = parser.parse(key, lines)
        assertEquals(expected, article)
    }

    @Test
    fun parseVariantWithExamples() {
        val raw = RawPart(
            "[trn]вызывать потускнение, лишать блеска; окислять[/trn]" +
                    "[ex][lang id=1033]tarnished by damp[/lang] — потускневший от влаги[/ex]" +
                    "[ex][lang id=1033]he couldn't bear to have his dream tarnished[/lang] " +
                    "— [p]образн.[/p] он не мог смириться с тем, что кто-то покушается на его мечту[/ex]"
        )
        val expected = VariantBuilder(pt("вызывать потускнение, лишать блеска; окислять"))
            .example("tarnished by damp", "1033", "потускневший от влаги")
            .example(
                "he couldn't bear to have his dream tarnished", "1033",
                st(ab("образн."), pt(" он не мог смириться с тем, что кто-то покушается на его мечту"))
            )
            .build()

        val variant = ArticleParser().parseVariant(raw)
        assertEquals(expected, variant)
    }
}