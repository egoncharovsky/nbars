package ru.egoncharovsky.nbars.entity.text

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SentenceTest {

    @Test
    fun `Plain texts should be joined`() {
        assertEquals(
            Sentence(listOf(PlainText("a"), Abbreviation("b"), PlainText("cd"))),
            Sentence.textFrom(listOf(PlainText("a"), Abbreviation("b"), PlainText("c"), PlainText("d")))
        )
    }

    @Test
    fun `Plain texts should be trimmed from start and end`() {
        assertEquals(
            Sentence(listOf(PlainText("a"), Abbreviation("B"), PlainText("b"))),
            Sentence.textFrom(listOf(PlainText("  a"), Abbreviation("B"), PlainText("b  ")))
        )
    }

    @Test
    fun `Plain texts should be removed from start and end if it is empty`() {
        assertEquals(
            Sentence(listOf(Abbreviation("B"))),
            Sentence.textFrom(listOf(PlainText("  "), Abbreviation("B"), PlainText("  ")))
        )
    }
}