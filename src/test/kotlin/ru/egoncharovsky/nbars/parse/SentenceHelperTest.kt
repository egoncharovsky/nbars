package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.ft
import ru.egoncharovsky.nbars.utils.SentenceHelper.pt
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr
import kotlin.test.assertEquals

class SentenceHelperTest {

    @Test
    fun testStringSentenceParse() {
        val expected = st(
            pt("("), ab("hi"), pt(" I am "), ft("fork", "12"),
            pt(" "), tr("FORK"), pt(") at "), ft("aaa", "2")
        )
        val sentence = st("(ab(hi) I am ft(fork,12) tr(FORK)) at ft(aaa,2)")

        assertEquals(expected, sentence)
    }
}