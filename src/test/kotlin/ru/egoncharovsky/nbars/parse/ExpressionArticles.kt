package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.utils.builder.article.ExpressionArticleBuilder

object ExpressionArticles {
    val tie_in = ExpressionArticleBuilder("tie in").homonyms {
        it.homonym("ˈtaɪˈɪn", ExpressionType.PHRASAL_VERB) {
            it.translation {
                it
                    .variant("соединять")
                    .variant("соединяться") {
                        it.example("to tie in with the main circuit", "1033", "ab(эл.) соединяться с сетью")
                    }
            }
            it.translation {
                it.variant("связываться, иметь связь") {
                    it.example(
                        "how does that statement tie in with what you said yesterday?",
                        "1033",
                        "какая связь между этим утверждением и тем, что вы сказали вчера?"
                    )
                }
            }
            it.translation {
                it.variant(
                    "присоединять (какой-л. товар) в качестве нагрузки при продаже другого товара; " +
                            "ab(≈) продавать с нагрузкой, продавать в качестве принудительного ассортимента"
                )
            }
        }
    }.build()

    val a_posteriori = ExpressionArticleBuilder("a posteriori").homonyms {
        it.homonym("ˌeɪpɒsterɪˈɔːr(a)ɪ", remark = "ab(лат.)") {
            it.translation { it.variant("апостериори, эмпирически, из опыта, по опыту") }
            it.translation { it.variant("апостериорный, основанный на опыте") }
        }
    }.build()
}