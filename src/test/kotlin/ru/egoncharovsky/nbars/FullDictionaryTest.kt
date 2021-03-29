package ru.egoncharovsky.nbars

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.parse.ArticleParser
import java.io.File
import java.lang.Exception

internal class FullDictionaryTest {

    private val dictionaryFile = File("src/main/resources/dictionary.dsl")
    private val indexFile = File("index/full_test.dsl.index")

    @Test
    internal fun fullDictionary() {
        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = ArticleParser()
        val positions = reader.readArticlePositions()

        positions.forEach { (headword, position) ->
            val lines = reader.readArticle(position)
            try {
                parser.parse(headword, lines)
            } catch (e: Exception) {
                throw Exception("Exception during parse $headword", e)
            }
        }
    }
}