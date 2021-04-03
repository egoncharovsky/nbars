package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.translation
import kotlin.test.assertEquals
import kotlin.test.expect

internal class RawPartTest {

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

    @Test
    fun cut() {
        val rawPart = RawPart("0123456789")

        val cut = rawPart.cut(1 until 5, 7..8)

        assertEquals(listOf(RawPart("1234"), RawPart("78")), cut)
        assertEquals(RawPart("0569"), rawPart)
    }

    @Test
    fun findBefore() {
        val rawPart = RawPart(
            "[p]зоол.[/p] [trn]марабу, аист индийский[/trn] [p]др.[/p] [p]цел.[/p]"
        )

        assertEquals(
            "зоол.",
            rawPart.findBefore(label, translation)
        )
    }

    @Test
    fun `Split should remove only group with defined number`() {
        val rawPart = RawPart(
            "[p]зоол.[/p]1.[trn]марабу, аист индийский[/trn] 2.[p]др.[/p] [trn]другой[/trn]3.fff"
        )

        val split = rawPart.split("[^ ](\\d+?\\.)".toRegex(), 1)
        assertEquals(listOf(
            RawPart("[p]зоол.[/p]"),
            RawPart("[trn]марабу, аист индийский[/trn] 2.[p]др.[/p] [trn]другой[/trn]"),
            RawPart("fff"),
        ), split)
    }

    @Test
    fun `Split with default group 0 should work as string split`() {
        val s = "1. a2. b3. c"
        val regex = "\\d+?\\.".toRegex()
        val rawPart = RawPart(s)

        val split = rawPart.split(regex)
        val expected = s.split(regex).map { RawPart(it.trim()) }
        assertEquals(expected, split)
    }

}