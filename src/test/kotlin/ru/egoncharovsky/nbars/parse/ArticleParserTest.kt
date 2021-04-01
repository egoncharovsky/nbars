package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.Pronoun
import kotlin.test.assertEquals

class ArticleParserTest {

    @Test
    fun parsePartOfSpeech() {
        val parser = ArticleParser()

        assertEquals(
            Pronoun(Pronoun.SubType.INDEFINITE),
            parser.parsePartOfSpeech(listOf("indef", "pron"))
        )
    }
}