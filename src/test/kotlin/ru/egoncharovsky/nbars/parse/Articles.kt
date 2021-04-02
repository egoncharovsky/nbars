package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.*
import ru.egoncharovsky.nbars.entity.GrammaticalForm.Plural
import ru.egoncharovsky.nbars.utils.ArticleBuilder
import ru.egoncharovsky.nbars.utils.SentenceHelper.ab
import ru.egoncharovsky.nbars.utils.SentenceHelper.ft
import ru.egoncharovsky.nbars.utils.SentenceHelper.pt
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

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

    val arry = ArticleBuilder("'Arry").homonyms {
        it.homonym("ˈærɪ", Noun) {
            it.translation { it.variant(st("= rf(Harry)"), remark = ab("прост.")) }
            it.translation {
                it.variant(
                    st("ко\u00B4кни, весёлый и не очень грамотный лондонец"),
                    remark = ab("пренебр.")
                )
            }
        }
    }.build()

    val narrator = ArticleBuilder("narrator")
        .homonyms {
            it.homonym("nəˈreɪtə", Noun) {
                it
                    .translation { it.variant("рассказчик; повествователь") }
                    .translation {
                        it.variant(
                            "ведущий; диктор; актёр, читающий текст от автора",
                            remark = "ab(театр.), ab(кино), ab(радио), ab(тлв.)"
                        )
                    }

            }
        }
        .build()

    val a_la = ArticleBuilder("a la").homonyms {
        it.homonym("ˈɑːlɑː", Adverb, remark = st("ab(фр.) à la")) {
            it
                .translation {
                    it.variant("а-ля; в стиле, в духе, во вкусе") {
                        it
                            .example("hairdo à la Marilyn Monroe", "1033", "причёска а-ля /под/ Мерилин Монро")
                            .example("à la Hollywood", "1033", "по-голливудски")
                            .example("demagoguery à la Hitler", "1033", "демагогия гитлеровского толка")
                    }
                }
                .translation {
                    it.variant("приготовленный на какой-л. манер", remark = "ab(кул.)") {
                        it
                            .example("à la parisienne (Boston)", "1033", "по-парижски (по-бостонски)")
                            .example("lobster à la king", "1033", "омар «кинг» (в белом соусе)")
                            .example("à la broche", "1033", "жаренный на вертеле")
                    }
                }
        }
    }.build()

    val abatement = ArticleBuilder("abatement")
        .homonyms {
            it.homonym("əˈbeɪtmənt", Noun) {
                it
                    .translation {
                        it.variant("ослабление, уменьшение; смягчение") {
                            it
                                .example("noise abatement campaign", "1033", "кампания за уменьшение (городского) шума")
                                .example("abatement of a storm", "1033", "затихание бури")
                                .example("abatement of the energies", "1033", "ослабление усилий")

                        }
                    }
                    .translation {
                        it
                            .variant("прекращение; устранение; отмена")
                            .variant("аннулирование, отмена, прекращение", remark = "ab(юр.)") {
                                it.example(
                                    "plea in abatement", "1033",
                                    "иск об аннулировании /отмене/ (права ab(и т. п.) — возражение ответчика против иска или его ходатайство о прекращении дела)"
                                )
                            }

                    }
                    .translation {
                        it.variant("скидка, снижение", remark = "ab(ком.)") {
                            it
                                .example("to make abatement", "1033", "сделать скидку, сбавить цену")
                                .example("no abatement made!", "1033", "по твёрдым ценам!, без запроса!")
                        }
                    }
            }
        }.build()

    val peet = ArticleBuilder("peet")
        .homonyms {
            it.reference("piːt", "= rf(pete)")
        }

        .build()

    val pence = ArticleBuilder("pence")
        .homonyms {
            it.reference("pens", "ab(от) rf(penny)", Plural)
        }
        .build()

    val excepting = ArticleBuilder("excepting")
        .homonyms {
            it
                .reference("ɪkˈseptɪŋ", "= rf(except) II 1", Preposition) {
                    it
                        .example("everyone not excepting myself", "1033", "все, в том числе и я")
                        .example("all were there not excepting him", "1033", "все были там, и он в том числе")
                }
                .reference("ɪkˈseptɪŋ", "= rf(except) III 1")
        }.build()

    val ll = ArticleBuilder("'ll").homonyms {
        it.reference("-ə(l)", "ab(разг.) ab(сокр.) ab(от) rf(will)2") {
            it.example("you'll be late", "1033", "вы опоздаете")
        }
    }.build()
}