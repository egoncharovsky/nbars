package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.utils.SentenceHelper
import ru.egoncharovsky.nbars.utils.MeaningVariantBuilder
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.pt
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.TranslationVariantBuilder
import kotlin.test.assertEquals

class TranslationParserTest {
    @Test
    fun parseVariantWithExamples() {
        val raw = RawPart(
            "[trn]вызывать потускнение, лишать блеска; окислять[/trn]" +
                    "[ex][lang id=1033]tarnished by damp[/lang] — потускневший от влаги[/ex]" +
                    "[ex][lang id=1033]he couldn't bear to have his dream tarnished[/lang] " +
                    "— [p]образн.[/p] он не мог смириться с тем, что кто-то покушается на его мечту[/ex]"
        )
        val expected = TranslationVariantBuilder(pt("вызывать потускнение, лишать блеска; окислять"))
            .example("tarnished by damp", "1033", "потускневший от влаги")
            .example(
                "he couldn't bear to have his dream tarnished", "1033",
                st(
                    ab("образн."),
                    pt(" он не мог смириться с тем, что кто-то покушается на его мечту")
                )
            )
            .build()

        val variant = TranslationParser().parseVariant(raw)
        assertEquals(expected, variant)
    }

    @Test
    fun parseVariantWithReference() {
        val raw = RawPart("[p]прост.[/p] = <<Harry>>")
        val expected = TranslationVariantBuilder(SentenceHelper.rf("Harry"), remark = ab("прост.")).build()

        val variant = TranslationParser().parseVariant(raw)
        assertEquals(expected, variant)
    }

    @Test
    fun parseMultipleCommentsInVariant() {
        val raw = RawPart("[com]([lang id=1033]the Greys[/lang])[/com] [p]pl[/p] " +
                "[com]([p]сокр.[/p] [p]от[/p] [lang id=1033]Scots Greys[/lang])[/com] " +
                "[trn]драгуны[/trn] " +
                "[com](2-й драгунский полк армии Великобритании)[/com]")
        val expected = TranslationVariantBuilder(
            pt("драгуны"),
            remark = ab("pl"),
            comment = st("(ft(the Greys,1033)) (ab(сокр.) ab(от) ft(Scots Greys,1033)) (2-й драгунский полк армии Великобритании)")
        ).build()

        val variant = TranslationParser().parseVariant(raw)
        assertEquals(expected, variant)
    }
}