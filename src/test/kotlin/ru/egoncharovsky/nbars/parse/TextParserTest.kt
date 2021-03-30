package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.egoncharovsky.nbars.entity.text.Reference
import ru.egoncharovsky.nbars.entity.text.Sentence
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt

internal class TextParserTest {

    @Test
    fun parseText() {
        val raw = RawPart("([lang id=1142]Leptoptilus[/lang]; [p]тж[/p] [lang id=1033]adjutant bird, adjutant stork[/lang])")

        val parser = TextParser()

        val text = parser.parse(raw)
        assertEquals(
            Sentence(pt("("), ft("Leptoptilus", "1142"), pt("; "), ab("тж"), pt(" "), ft("adjutant bird, adjutant stork", "1033"), pt(")")),
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