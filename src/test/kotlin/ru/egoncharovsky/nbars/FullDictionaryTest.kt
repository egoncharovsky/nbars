package ru.egoncharovsky.nbars

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.article.DictionaryArticle
import ru.egoncharovsky.nbars.entity.article.WordArticle
import ru.egoncharovsky.nbars.entity.article.section.SpecializedVocabulary
import ru.egoncharovsky.nbars.parse.*
import java.io.File
import kotlin.system.measureTimeMillis

internal class FullDictionaryTest {

    private val logger = KotlinLogging.logger { }

    private val dictionaryFile = File("src/main/resources/dictionary.dsl")
    private val indexFile = File("index/full_test.dsl.index")

    @Test
    fun article() {
        val key = "ought"

        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = DictionaryParser()
        val positions = reader.readArticlePositions()
        val headwords = positions.keys.toList()

        val article = parser.parse(key, reader.readArticle(positions[key]!!))
        println()
    }

    @Test
    fun fullDictionary() {
        val reader = DictionaryReader(dictionaryFile, indexFile)
        val parser = DictionaryParser()
        val positions = reader.readArticlePositions()
        val headwords = positions.keys.toList()

        val printErrorOnLines: Set<String> = setOf("ArticleParser.kt:89")
        val isShortArticle: (List<String>) -> Boolean = { it.size < 10 }
        var shortArticlesCount = 0
        var longArticlesCount = 0

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

        val sp = results.toMap()
            .mapValues { (_, result) -> result.getOrNull() }
            .filterValues { it != null }
            .filter { (_, article) ->
                article is WordArticle && article.homonyms.flatten().any { it is SpecializedVocabulary }
            }
            .map { (headword, _) ->
                "$headword\n\t${reader.readArticle(positions[headword]!!).joinToString("\n\t")}\n"
            }
        File("1.txt").writeText(sp.joinToString("\n"))

        results.toMap()
            .mapValues { (_, result) -> result.exceptionOrNull() }
            .filterValues { it != null }
            .forEach { (headword, exception) ->
                val element =
                    exception!!.stackTrace.find {
                        it.className == ArticleParser::class.qualifiedName
                                || it.className == ExpressionArticleParser::class.qualifiedName
                                || it.className == TranslationParser::class.qualifiedName
                                || it.className == ReferenceToArticleParser::class.qualifiedName
                                || it.className == MorphemeArticleParser::class.qualifiedName
                    } ?: exception.stackTrace.find { it.className == DictionaryParser::class.qualifiedName }!!
                val fileName = element.fileName
                val line = element.lineNumber
                val message = exception.message!!

                if (printErrorOnLines.contains("$fileName:$line")) {
//                    logger.error("'$headword': \t$message at $element (#${headwords.indexOf(headword)})")
                    val a = reader.readArticle(positions[headword]!!)
                    if (isShortArticle(a)) {
                        logger.error("'$headword': \t$message at $element (#${headwords.indexOf(headword)})\n${
                            a.joinToString("\n",
                                postfix = "\n")
                        }")
                        shortArticlesCount++
                    } else {
                        longArticlesCount++
                    }
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
        logger.error("Short articles: $shortArticlesCount long articles: $longArticlesCount")
        logger.error("Specialized: ${sp.size}")
        logger.error("Total errors: $errors (${errors * 100 / positions.size} %)")
    }

    private fun seconds(millis: Long) = "${millis / 1000}.${millis % 1000} s"
}