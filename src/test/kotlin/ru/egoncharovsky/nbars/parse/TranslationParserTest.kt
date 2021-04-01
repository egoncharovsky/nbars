package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.utils.SentenceHelper
import ru.egoncharovsky.nbars.utils.VariantBuilder

class TranslationParserTest {
    @Test
    fun parseVariantWithExamples() {
        val raw = RawPart(
            "[trn]вызывать потускнение, лишать блеска; окислять[/trn]" +
                    "[ex][lang id=1033]tarnished by damp[/lang] — потускневший от влаги[/ex]" +
                    "[ex][lang id=1033]he couldn't bear to have his dream tarnished[/lang] " +
                    "— [p]образн.[/p] он не мог смириться с тем, что кто-то покушается на его мечту[/ex]"
        )
        val expected = VariantBuilder(SentenceHelper.pt("вызывать потускнение, лишать блеска; окислять"))
            .example("tarnished by damp", "1033", "потускневший от влаги")
            .example(
                "he couldn't bear to have his dream tarnished", "1033",
                SentenceHelper.st(
                    SentenceHelper.ab("образн."),
                    SentenceHelper.pt(" он не мог смириться с тем, что кто-то покушается на его мечту")
                )
            )
            .build()

        val variant = TranslationParser().parseVariant(raw)
        kotlin.test.assertEquals(expected, variant)
    }

    @Test
    fun parseVariantWithReference() {
        val raw = RawPart("[p]прост.[/p] = <<Harry>>")
        val expected = VariantBuilder(SentenceHelper.rf("Harry"), remark = SentenceHelper.ab("прост.")).build()

        val variant = TranslationParser().parseVariant(raw)
        kotlin.test.assertEquals(expected, variant)
    }
}