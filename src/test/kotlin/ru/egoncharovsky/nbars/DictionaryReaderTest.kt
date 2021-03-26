package ru.egoncharovsky.nbars

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class DictionaryReaderTest {

    @TempDir
    lateinit var indexDir: Path

    private val dictionaryFile = File(javaClass.classLoader.getResource("dictionary/test.dsl")!!.toURI())
    private val indexFile by lazy { File(indexDir.toFile(), "test.dsl.index") }

    @Test
    fun `Reader should scan all headwords`() {
        val reader = DictionaryReader(dictionaryFile, indexFile)

        val positions = reader.scanArticlePositions()

        assertEquals(
            setOf(
                "'Arry",
                "'cause",
                "(')celli",
                "'cellist",
                "'cello",
                "'cep",
                "'varsity",
                "varsity",
                "run",
                "Условные обозначения"
            ),
            positions.keys
        )
    }

    @Test
    fun `After read article positions index should be created`() {
        val reader = DictionaryReader(dictionaryFile, indexFile)

        reader.readArticlePositions()
        assertTrue(indexFile.exists())

        assertEquals(
            listOf(
                "4614b662118d563bedbc1dcacf690985",
                """'Arry:77,'cause:299,(')celli:411,'cellist:526,'cello:697,'cep:1182,'varsity:1343,varsity:1352,run:1682,Условные обозначения:80388"""
            ), indexFile.readLines()
        )
    }

    @Test
    fun `Read article should return full card body`() {
        val runCardBody = readResourceLines("card/run.dsl")
        val reader = DictionaryReader(dictionaryFile, indexFile)

        val positions = reader.readArticlePositions()
        val article = reader.readArticle(positions["run"]!!)

        assertEquals(
            runCardBody,
            article
        )
    }
}