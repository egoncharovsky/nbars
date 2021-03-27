package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.PlainText
import ru.egoncharovsky.nbars.entity.text.Sentence
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.readResourceLines

internal class ArticleParserTest {

    @Test
    fun parse() {
        val key = "adjutant"
        val lines = readResourceLines("card/adjutant.dsl")

        val parser = ArticleParser()

        val article = parser.parse("adjutant", lines)
        println()
    }
}