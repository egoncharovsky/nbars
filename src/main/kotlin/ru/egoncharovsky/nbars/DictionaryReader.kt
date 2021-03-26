package ru.egoncharovsky.nbars

import com.twmacinta.util.MD5
import mu.KotlinLogging
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.RandomAccessFile
import java.nio.charset.Charset
import kotlin.system.measureTimeMillis

class DictionaryReader(
    private val dictionaryFile: File,
    private val indexFile: File
) {

    object IndexSeparator {
        const val entry = ","
        const val keyValue = ":"
    }

    private val headwordMarker = "^[^\\t#]".toRegex()
    private val endMarker = "\\{\\{ The End }}".toRegex()
    private val braces = "[{}]".toRegex()

    private val logger = KotlinLogging.logger { }

    fun readArticlePositions(): Map<String, Long> {
        val positions: Map<String, Long>
        val time = measureTimeMillis {
            val dictionaryHash = MD5.asHex(MD5.getHash(dictionaryFile))
            logger.debug("MD5 dictionary hash: $dictionaryHash")

            positions = if (indexFile.exists()) {
                val lines = indexFile.readLines()
                val indexHash = lines[0]
                if (indexHash == dictionaryHash) {
                    restoreArticlePositions(lines)
                } else {
                    logger.debug("Dictionary changed, rescan: dictionary MD5 $dictionaryHash index MD5 $indexHash")
                    scanArticlePositions().also {
                        saveArticlePositionsIndex(dictionaryHash, it)
                    }
                }
            } else {
                logger.debug("Index doesn't exist")
                scanArticlePositions().also {
                    saveArticlePositionsIndex(dictionaryHash, it)
                }
            }
        }
        logger.debug("Article positions read in ${seconds(time)}")
        return positions
    }

    fun scanArticlePositions(): Map<String, Long> {
        logger.debug("Scanning dictionary...")

        return RandomAccessFile(dictionaryFile, "r").use { raf ->

            val headwordPositions = mutableMapOf<String, Long>()
            while (raf.filePointer < raf.length()) {
                val lineStart = raf.filePointer
                val line = raf.readLine(charset("UTF-8"))

                if (line != null) {
                    if (endMarker.containsMatchIn(line)) break
                    if (headwordMarker.containsMatchIn(line)) {
                        val headword = line.trim().replace(braces, "")
                        headwordPositions[headword] = lineStart
                    }
                }
            }
            logger.debug("Headwords found total: ${headwordPositions.size}")
            headwordPositions
        }.toMap()
    }

    fun clearIndex() = if (indexFile.exists()) indexFile.delete() else true

    fun readArticle(position: Long): List<String> {
        logger.debug("Read article at $position")

        val article: List<String>
        val time = measureTimeMillis {
            article = RandomAccessFile(dictionaryFile, "r").use { readCardLines(it, position) }
        }
        logger.debug("Article read in ${seconds(time)}")
        return article
    }

    fun readCards(positions: List<Long>): List<List<String>> {
        logger.debug("Read ${positions.size} articles")

        val articles: List<List<String>>
        val time = measureTimeMillis {
            articles = RandomAccessFile(dictionaryFile, "r").use { raf ->
                positions.map { readCardLines(raf, it) }
            }
        }
        logger.debug("Articles read in ${seconds(time)}")
        return articles
    }

    private fun readCardLines(raf: RandomAccessFile, position: Long): List<String> {
        raf.seek(position)
        val articleLines = mutableListOf<String>()

        while (raf.filePointer < raf.length()) {
            val line = raf.readLine(charset("UTF-8"))

            if (line != null) {
                if (line.isEmpty()) break
                if (headwordMarker.containsMatchIn(line)) {
                    continue
                }
                articleLines.add(line)
            }
        }
        return articleLines
    }

    private fun restoreArticlePositions(indexLines: List<String>): Map<String, Long> {
        logger.debug("Restoring index")

        return indexLines[1].split(IndexSeparator.entry).map {
             val split = it.split(IndexSeparator.keyValue)

            val key = split[0]
            val position = split[1].toLong()

            key to position
        }.toMap()
    }

    private fun saveArticlePositionsIndex(hash: String, positions: Map<String, Long>) {
        logger.debug("Saving index")
        val index = positions.map { (key, value) ->
            "$key${IndexSeparator.keyValue}$value"
        }.joinToString(IndexSeparator.entry)

        indexFile.writeText("$hash\n$index")
    }

    private fun seconds(millis: Long) = "${millis / 1000}.${millis % 1000} s"

    private fun RandomAccessFile.readLine(charset: Charset): String? {
        val bytes = ByteArrayOutputStream()

        var c = -1
        var eol = false

        while (!eol) {
            when (read().also { c = it }) {
                -1, '\n'.toInt() -> eol = true
                '\r'.toInt() -> {
                    eol = true
                    val cur = filePointer
                    if (read() != '\n'.toInt()) {
                        seek(cur)
                    }
                }
                else -> bytes.write(c)
            }
        }

        return if (c == -1 && bytes.size() == 0) {
            null
        } else {
            bytes.toString(charset)
        }
    }
}