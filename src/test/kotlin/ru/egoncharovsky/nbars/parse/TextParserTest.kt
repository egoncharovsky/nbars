package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.text.Reference
import ru.egoncharovsky.nbars.parse.SentenceHelper.ab
import ru.egoncharovsky.nbars.parse.SentenceHelper.ft
import ru.egoncharovsky.nbars.parse.SentenceHelper.st

internal class TextParserTest {

    @Test
    fun parseText() {
        val raw =
            RawPart("([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])")

        val parser = TextParser()

        val text = parser.parse(raw)
        assertEquals(
            st("(", ft("Leptoptilus", "1142"), "; ", ab("тж"), " ", ft("adjutant bird, adjutant stork", "1033"), ")"),
            text
        )
    }

    @Test
    fun parseReference() {
        val raw = RawPart("<<Apple>>")

        val parser = TextParser()

        val text = parser.parse(raw)
        assertEquals(
            Reference("Apple"),
            text
        )
    }
}