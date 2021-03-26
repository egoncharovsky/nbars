package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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