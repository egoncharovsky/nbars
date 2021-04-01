package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.utils.VariantBuilder
import ru.egoncharovsky.nbars.entity.Pronoun
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.pt
import ru.egoncharovsky.nbars.utils.SentenceHelper.rf
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import kotlin.test.assertEquals

class ArticleParserTest {

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
                st(
                    ab("образн."),
                    pt(" он не мог смириться с тем, что кто-то покушается на его мечту")
                )
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