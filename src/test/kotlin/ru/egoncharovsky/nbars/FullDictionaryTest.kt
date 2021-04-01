package ru.egoncharovsky.nbars

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.article.DictionaryArticle
import ru.egoncharovsky.nbars.parse.DictionaryParser
import java.io.File
import kotlin.system.measureTimeMillis

internal class FullDictionaryTest {

    private val logger = KotlinLogging.logger { }

    private val dictionaryFile = File("src/main/resources/dictionary.dsl")
    private val indexFile = File("index/full_test.dsl.index")

    @Test
    fun fullDictionary() {
        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = DictionaryParser()
        val positions = reader.readArticlePositions()
        val headwords = positions.keys.toList()

        val printErrorOnLines = setOf<Int>(82)

        val results: List<Pair<String, Result<DictionaryArticle>>>
        val time = measureTimeMillis {
            results = runBlocking(Dispatchers.IO) {
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

        val exceptionLines = mutableMapOf<StackTraceElement, Int>()
        val exceptionMessages = mutableMapOf<StackTraceElement, String>()
        val exceptions = mutableMapOf<StackTraceElement, MutableSet<Throwable>>()

        val a = results.toMap()

        results.toMap()
            .mapValues { (_, result) -> result.exceptionOrNull() }
            .filterValues { it != null }
            .forEach { (headword, exception) ->
                val element =
                    exception!!.stackTrace.find { it.className == "ru.egoncharovsky.nbars.parse.ArticleParser" }!!
                val line = element.lineNumber
                val message = exception.message!!

                if (printErrorOnLines.contains(line)) {
                    logger.error("$message at $element for '$headword' (#${headwords.indexOf(headword)})")
                }

                exceptionLines.putIfAbsent(element, 0)
                exceptionLines[element] = exceptionLines[element]!! + 1

                exceptionMessages.putIfAbsent(element, "for '$headword' (#${headwords.indexOf(headword)}): $message")

                exceptions.putIfAbsent(element, mutableSetOf())
                exceptions[element]!!.add(exception)
            }
        logger.info("Finished in ${seconds(time)}")

        val errors = exceptionLines.values.sum()
        val sortedLines = exceptionLines.toList()
            .sortedByDescending { (_, value) -> value }
        val frequency = sortedLines.joinToString("\n") { (element, count) ->
            "$count\t\tat $element, ${exceptions[element]}"
        }
        val types = sortedLines.map { Triple(it.first, exceptions[it.first]!!, it.second) }
            .joinToString("\n\n") { (element, exs, count) ->
                "$count at $element: " + exs.joinToString("\n\t", "\n\t")
            }

        logger.error("Exceptions occurred at\n$frequency")
        logger.error("Exception types:\n$types")
        logger.error("Total errors: $errors (${errors * 100 / positions.size} %)")
    }

    private fun seconds(millis: Long) = "${millis / 1000}.${millis % 1000} s"
}