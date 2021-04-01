package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.translation
import kotlin.test.assertEquals

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
}