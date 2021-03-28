package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.egoncharovsky.nbars.ArticleBuilder
import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.st
import ru.egoncharovsky.nbars.readResourceLines
import kotlin.test.assertEquals

internal class ArticleParserTest {

    companion object {
        @JvmStatic
        fun parameters() = listOf(
            "adjutant" to ArticleBuilder("adjutant")
                .homonyms {
                    it.homonym("ˈæʤʊt(ə)nt", "n") {
                        it.translation(ab("воен.")) {
                            it
                                .variant(pt("адъютант"))
                                .variant(
                                    st(
                                        pt("начальник строевого отдела "), ab("или"),
                                        pt(" отделения личного состава")
                                    )
                                )
                        }.translation {
                            it.variant(pt("помощник, ассистент"), remark = ab("арх."))
                        }
                    }.homonym("ˈæʤʊt(ə)nt", "a") {
                        it.translation {
                            it.variant(pt("оказывающий помощь, помогающий, содействующий"), remark = ab("редк."))
                        }
                    }
                }.homonyms {
                    it.homonym("ˈæʤʊt(ə)nt", "n") {
                        it.translation {
                            it.variant(
                                pt("марабу, аист индийский"), remark = ab("зоол."), comment = st(
                                    pt("("),
                                    ft("Leptoptilus", "1142"), pt("; "),
                                    ab("тж"), pt(" "),
                                    ft("adjutant bird, adjutant stork", "1033"),
                                    pt(")")
                                )
                            )
                        }
                    }
                }.build()
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