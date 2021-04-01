package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.utils.ExpressionArticleBuilder

object ExpressionArticles {
    val tie_in = ExpressionArticleBuilder("tie in", "ˈtaɪˈɪn", ExpressionType.PHRASAL_VERB)
        .translation {
            it
                .variant("соединять")
                .variant("соединяться") {
                    it.example("to tie in with the main circuit", "1033", "ab(эл.) соединяться с сетью")
                }
        }
        .translation {
            it.variant("связываться, иметь связь") {
                it.example(
                    "how does that statement tie in with what you said yesterday?",
                    "1033",
                    "какая связь между этим утверждением и тем, что вы сказали вчера?"
                )
            }
        }
        .translation {
            // is it need to parse comments in translation sentence?
            it.variant("присоединять (какой-л. товар) в качестве нагрузки при продаже другого товара; " +
                    "ab(≈) продавать с нагрузкой, продавать в качестве принудительного ассортимента")
        }.build()
}