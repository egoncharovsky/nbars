package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.Pronoun
import ru.egoncharovsky.nbars.parse.article.WordArticleParser
import kotlin.test.assertEquals

class WordWordArticleParserTest {

    @Test
    fun parsePartOfSpeech() {
        val parser = WordArticleParser()

        assertEquals(
            Pronoun(Pronoun.SubType.INDEFINITE),
            parser.parsePartOfSpeech(listOf("indef", "pron"))
        )
    }
}