package ru.egoncharovsky.nbars

import kotlinx.coroutines.*
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.parse.ArticleParser
import java.io.File
import kotlin.system.measureTimeMillis

internal class FullDictionaryTest {

    private val logger = KotlinLogging.logger { }

    private val dictionaryFile = File("src/main/resources/dictionary.dsl")
    private val indexFile = File("index/full_test.dsl.index")

    @Test
    fun fullDictionary() {
        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = ArticleParser()
        val positions = reader.readArticlePositions()
        val headwords = positions.keys.toList()

        val results: List<Pair<String, Result<Article>>>
        val time = measureTimeMillis {
            results = runBlocking {
                withContext(Dispatchers.IO) {
                    positions.map { (headword, position) ->
                        async {
                            val lines = reader.readArticle(position)
                            headword to kotlin.runCatching {
                                parser.parse(headword, lines)
                            }
                        }
                    }.awaitAll()
                }
            }
        }

        val exceptionLines = mutableMapOf<StackTraceElement, Int>()
        val exceptionMessages = mutableMapOf<StackTraceElement, String>()


        results.forEach { (headword, result) ->
            result.exceptionOrNull()?.let {
                val element =
                    it.stackTrace.find { it.className == "ru.egoncharovsky.nbars.parse.ArticleParser" }!!
                exceptionLines.putIfAbsent(element, 0)
                exceptionMessages.putIfAbsent(element, "for '$headword' (#${headwords.indexOf(headword)}): ${it.message!!}")
                exceptionLines[element] = exceptionLines[element]!! + 1
            }
        }
        logger.info("Finished in ${seconds(time)}")

        val errors = exceptionLines.values.sum()
        val sorted = exceptionLines.toList()
            .sortedByDescending { (_, value) -> value }
            .joinToString("\n") { (element, count) -> "$count\t\tat $element, ${exceptionMessages[element]}" }

        logger.error("Exceptions occurred at\n$sorted")
        logger.error("Total errors: $errors (${errors * 100 / positions.size} %)")
    }

    private fun seconds(millis: Long) = "${millis / 1000}.${millis % 1000} s"
}