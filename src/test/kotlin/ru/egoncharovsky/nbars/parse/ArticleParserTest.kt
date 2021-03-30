package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.VariantBuilder
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.Pronoun
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.rf
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.st
import ru.egoncharovsky.nbars.getResource
import kotlin.test.assertEquals

internal class ArticleParserTest {

    companion object {
        @JvmStatic
        fun parameters() = listOf(
            Articles.adjutant,
            Articles.tarnish,
            Articles.someone
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

    @Test
    fun parseVariantWithReference() {
        val raw = RawPart("[p]прост.[/p] = <<Harry>>")
        val expected = VariantBuilder(rf("Harry"), remark = ab("прост.")).build()

        val variant = ArticleParser().parseVariant(raw)
        assertEquals(expected, variant)
    }

    @Test
    fun parsePartOfSpeech() {
        val parser = ArticleParser()

        assertEquals(
            Pronoun(Pronoun.SubType.INDEFINITE),
            parser.parsePartOfSpeech(listOf("indef", "pron"))
        )
    }
}