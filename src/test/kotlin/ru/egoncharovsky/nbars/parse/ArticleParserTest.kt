package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.Homonym
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.Translation.Variant
import ru.egoncharovsky.nbars.entity.text.Abbreviation
import ru.egoncharovsky.nbars.entity.text.Sentence
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.readResourceLines
import kotlin.test.assertEquals

internal class ArticleParserTest {

    companion object {
        @JvmStatic
        fun parameters() = listOf(
            "adjutant" to Article(
                "adjutant", listOf(
                    listOf(
                        Homonym(
                            "ˈæʤʊt(ə)nt", "n", listOf(
                                Translation(
                                    remark = Abbreviation("воен."),
                                    variants = listOf(
                                        Variant(pt("адъютант")),
                                        Variant(
                                            Sentence(
                                                pt("начальник строевого отдела "),
                                                ab("или"),
                                                pt(" отделения личного состава")
                                            )
                                        )
                                    )
                                ),
                                Translation(
                                    listOf(
                                        Variant(
                                            remark = ab("арх."),
                                            meaning = pt("помощник, ассистент")
                                        )
                                    )
                                )
                            )
                        ),
                        Homonym(
                            "ˈæʤʊt(ə)nt", "a", listOf(
                                Translation(
                                    variants = listOf(
                                        Variant(
                                            remark = ab("редк."),
                                            meaning = pt("оказывающий помощь, помогающий, содействующий")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    listOf(
                        Homonym(
                            "ˈæʤʊt(ə)nt", "n", translations = listOf(
                                Translation(
                                    variants = listOf(
                                        Variant(
                                            remark = ab("зоол."),
                                            meaning = pt("марабу, аист индийский"),
                                            comment = Sentence(
                                                pt("("),
                                                ft("Leptoptilus", "1142"),
                                                pt("; "),
                                                ab("тж"),
                                                pt(" "),
                                                ft("adjutant bird, adjutant stork", "1033"),
                                                pt(")")
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ).map { arrayOf(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun parse(key: String, expected: Article) {
        val lines = readResourceLines("card/$key.dsl")
        val parser = ArticleParser()

        val article = parser.parse(key, lines)
        assertEquals(expected, article)
    }
}