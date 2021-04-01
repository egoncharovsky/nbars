package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.ArticleBuilder
import ru.egoncharovsky.nbars.entity.Adjective
import ru.egoncharovsky.nbars.entity.Noun
import ru.egoncharovsky.nbars.entity.Pronoun
import ru.egoncharovsky.nbars.entity.Verb
import ru.egoncharovsky.nbars.parse.SentenceHelper.ab
import ru.egoncharovsky.nbars.parse.SentenceHelper.ft
import ru.egoncharovsky.nbars.parse.SentenceHelper.pt
import ru.egoncharovsky.nbars.parse.SentenceHelper.st
import ru.egoncharovsky.nbars.parse.SentenceHelper.tr

object Articles {
    val adjutant = ArticleBuilder("adjutant")
        .homonyms {
            it.homonym("ˈæʤʊt(ə)nt", Noun) {
                it.translation(ab("воен.")) {
                    it
                        .variant("адъютант")
                        .variant(
                            st(pt("начальник строевого отдела "), ab("или"), pt(" отделения личного состава"))
                        )
                }.translation {
                    it.variant(pt("помощник, ассистент"), remark = ab("арх."))
                }
            }.homonym("ˈæʤʊt(ə)nt", Adjective) {
                it.translation {
                    it.variant(pt("оказывающий помощь, помогающий, содействующий"), remark = ab("редк."))
                }
            }
        }.homonyms {
            it.homonym("ˈæʤʊt(ə)nt", Noun) {
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
            it.homonym("ˈtɑːnɪʃ", Noun) {
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
            }.homonym("ˈtɑːnɪʃ", Verb) {
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

    val someone = ArticleBuilder("someone")
        .homonyms {
            it.homonym("ˈsʌmwʌn, ˈsʌmwən", Pronoun(Pronoun.SubType.INDEFINITE)) {
                it.translation {
                    it.variant("кто-то, кто-нибудь, кто-либо") {
                        it
                            .example("someone else", "1033", "кто-то другой")
                            .example("someone else's", "1033", "чужой, не свой")
                            .example("someone or other", "1033", "тот или иной; кто-нибудь, кто-либо")
                            .example(
                                "someone has to lock up the house",
                                "1033",
                                "кто-нибудь /кто-то/ должен запереть дом"
                            )
                    }
                }
            }
        }
        .build()

    val swagman = ArticleBuilder("swagman").homonyms {
        it.homonym(
            "ˈswægmæn", Noun,
            comment = st("(", ab("pl"), " ", ft("-men", "1033"), " ", tr("ˈswægmen"), pt(")"))
        ) {
            it.translation {
                it.variant(
                    pt("свагмен, человек, всё добро которого помещается в скатанном одеяле за плечами; бродяга"),
                    remark = ab("австрал.")
                )
            }
        }
    }.build()
}