package ru.egoncharovsky.nbars

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.parse.ArticleParser
import java.io.File
import java.lang.Exception

internal class FullDictionaryTest {

    private val logger = KotlinLogging.logger {  }

    private val dictionaryFile = File("src/main/resources/dictionary.dsl")
    private val indexFile = File("index/full_test.dsl.index")

    @Test
    fun fullDictionary() {
        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = ArticleParser()
        val positions = reader.readArticlePositions()
        var i=1
        var errors=0

        positions.forEach { (headword, position) ->
            val lines = reader.readArticle(position)
            try {
                parser.parse(headword, lines)
                i++
            } catch (e: Exception) {
                logger.error("Exception during parse '$headword' ($i from ${positions.size}): ${e.message}")
                errors++
//                throw Exception("Exception during parse '$headword' ($i from ${positions.size})", e)
            }
        }
        logger.info("Total errors: $errors (${errors*100 / positions.size} %)")
    }
}