package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.text.Reference
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.eng
import ru.egoncharovsky.nbars.utils.SentenceHelper.ft
import ru.egoncharovsky.nbars.utils.SentenceHelper.st

internal class TextParserTest {

    @Test
    fun parseSentence() {
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

    @Test
    fun parseForeignTextWithTranscription() {
        val raw = RawPart("([p]pl[/p] [lang id=1033]-tos \\[[t]ˈæntɪˌpæstəʊz[/t]\\], -ti[/lang])")
        val parser = TextParser()

        val text = parser.parse(raw)

        assertEquals(
            st("(ab(pl) ft(-tos (tr(ˈæntɪˌpæstəʊz)), -ti,1033))"),
            text
        )
    }

    @Test
    fun parseForeignTextWithLabel() {
        val raw = RawPart("[lang id=1033]oatmeal biscuits /[p]амер.[/p] cookies/[/lang]")
        val parser = TextParser()

        val text = parser.parse(raw)

        assertEquals(
            eng("oatmeal biscuits /ab(амер.) cookies/"),
            text
        )
    }
}