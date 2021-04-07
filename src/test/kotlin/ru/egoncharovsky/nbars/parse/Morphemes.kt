package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.utils.MorphemeBuilder

object Morphemes {
    val ible = MorphemeBuilder("-ible")
        .reference("-əb(ə)l", "= rf(-able) 2, 3")

    val ade = MorphemeBuilder("-ade")
        .homonym(
            "-eɪd",
            MorphemeType.SUFFIX,
            "выделяется в ряде существительных, заимствованных из французского или испанского языка и обозначающих:"
        ) {
            it.translation {
                it.variant("действие или процесс") {
                    it.example("cannonade", "1033", "канонада")
                    it.example("masquerade", "1033", "маскарад")
                }
            }
            it.translation {
                it.variant("оценку деятельности или поведения") {
                    it.example("renegade", "1033", "ренегат")
                }
            }
            it.translation {
                it.variant("фруктовый напиток") {
                    it.example("lemonade", "1033", "лимонад")
                    it.example("orangeade", "1033", "оранжад")
                }
            }
        }.build()

    val ad = MorphemeBuilder("-ad")
        .homonym("-æd, -əd", MorphemeType.SUFFIX) {
            it.translation {
                it.variant("встречается в ab(сущ.), производных от ab(греч.) числительных, со значением группа с таким-то числом членов") {
                    it.example("dyad", "1033", "диада")
                    it.example("triad", "1033", "триада")
                    it.example("pentad", "1033", "пентада")
                }
            }

        }
        .homonym("-æd, -əd", MorphemeType.SUFFIX) {
            it.translation {
                it.variant("по направлению к", remark = "ab(биол.)", comment = "c") {
                    it.example("ventrad", "1033", "по направлению к брюшной стороне")
                    it.example("cephalad", "1033", "краниально, по направлению к голове")
                }
            }
        }.build()
}