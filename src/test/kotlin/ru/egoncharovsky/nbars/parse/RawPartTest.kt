package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RawPartTest {

    private val translation = "\\[trn](.+?)\\[/trn]".toRegex()

    @Test
    fun before() {
        val rawPart = RawPart(
            "[p]зоол.[/p] [trn]марабу, аист индийский[/trn] " +
                    "([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])"
        )

        val result = rawPart.before(translation)

        assertEquals(
            Pair(
                RawPart("[p]зоол.[/p] "),
                RawPart("[trn]марабу, аист индийский[/trn] ([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])")
            ),
            result
        )
    }

//    @Test
//    fun `Raw should be divided by regex into trimmed parts`() {
//        val rawPart = RawPart(
//            "[p]зоол.[/p][trn]марабу, аист индийский[/trn] " +
//                    "([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])"
//        )
//
//        val result = rawPart.divide(translation, 3)
//
//        assertEquals(
//            listOf(
//                "[p]зоол.[/p]",
//                "[trn]марабу, аист индийский[/trn]",
//                "([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])"
//            ).map { RawPart(it) },
//            result
//        )
//    }
//
//    @Test
//    fun `Raw should be successfully divide into more then 3 parts`() {
//        val rawPart = RawPart("a b a b a ")
//
//        val result = rawPart.divide("b".toRegex(), 5)
//
//        assertEquals(
//            listOf("a", "b", "a", "b", "a").map { RawPart(it) },
//            result
//        )
//    }
//
//    @Test
//    fun `If occurrences is not enough division should be completed to 'into' count`() {
//        val rawPart = RawPart("a b a")
//
//        val result = rawPart.divide("b".toRegex(), 5)
//
//        assertEquals(
//            listOf("a", "b", "a", "", "").map { RawPart(it) },
//            result
//        )
//    }
//
//    @Test
//    fun `If no occurrences should returns list with original raw part`() {
//        val rawPart = RawPart("a a a")
//
//        val result = rawPart.divide("b".toRegex(), 5)
//
//        assertEquals(
//            listOf(rawPart),
//            result
//        )
//    }
}