package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.ArticleBuilder
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ab
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.ft
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.pt
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.st

object Articles {
    val adjutant = ArticleBuilder("adjutant")
        .homonyms {
            it.homonym("ˈæʤʊt(ə)nt", "n") {
                it.translation(ab("воен.")) {
                    it
                        .variant("адъютант")
                        .variant(
                            st(pt("начальник строевого отдела "), ab("или"), pt(" отделения личного состава"))
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
                            pt("("), ft("Leptoptilus", "1142"), pt("; "),
                            ab("тж"), pt(" "), ft("adjutant bird, adjutant stork", "1033"), pt(")")
                        )
                    )
                }
            }
        }.build()

    val tarnish = ArticleBuilder("tarnish")
        .homonyms {
            it.homonym("ˈtɑːnɪʃ", "n") {
                it
                    .translation {
                        it
                            .variant("тусклость; матовость; неяркость")
                            .variant("тусклое пятно")
                            .variant("тусклая, матовая поверхность")
                    }
                    .translation { it.variant("налёт, потускнение") }
                    .translation {
                        it.variant("позорное пятно, тень") {
                            it.example("there's no tarnish on them", "1033", "они ничем себя не запятнали")
                        }
                    }
            }.homonym("ˈtɑːnɪʃ", "v") {
                it.translation {
                    it
                        .variant("вызывать потускнение, лишать блеска; окислять") {
                            it
                                .example("tarnished by damp", "1033", "потускневший от влаги")
                                .example(
                                    "he couldn't bear to have his dream tarnished", "1033", st(
                                        ab("образн."),
                                        pt(" он не мог смириться с тем, что кто-то покушается на его мечту")
                                    )
                                )
                        }
                        .variant("тускнеть, терять блеск; окисляться")
                }.translation {
                    it.variant("порочить, пятнать, бросать тень; позорить") {
                        it.example(
                            "to tarnish one's honour \\[one's reputation, one's name\\]", "1033",
                            "запятнать свою честь \\[репутацию, своё имя\\]"
                        )
                    }
                }
            }
        }
        .build()
}