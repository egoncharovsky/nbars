package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.eng
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

    val blue_murder = ExpressionArticleBuilder("blue murder").homonyms {
        it.homonym("ˌbluːˈmɜːdə", remark = "ab(сл.)") {
            it.translation {
                it.variant("караул") {
                    it.example(
                        eng("to scream /to cry, to shout, to yell/ blue murder"),
                        "а) кричать караул; поднимать шум /вопль/; б) орать, дико вопить (без особой причины)"
                    )
                }
            }
            it.translation(remark = ab("амер.")) {
                it.variant("полное поражение, провал")
                it.variant("трудная задача")
            }
            it.idiom(eng("like blue murder"), "чертовски быстро, со всех ног, сломя голову")
        }
    }.build()

    val ad_libitum = ExpressionArticleBuilder("ad libitum").homonyms {
        it.homonym("ˌædˈlɪbɪtəm", remark = "ab(лат.)") {
            it.translation { it.variant("= rf(ad lib) I 1) и 2)") }
            it.translation(remark = ab("муз.")) {
                it.variant(
                    "ад либитум, на усмотрение исполнителя",
                    comment = "(о темпе, громкости ab(и т. п.))"
                )
                it.variant("разрешение исключить часть или партию музыкального произведения")
            }
        }
    }.build()
}