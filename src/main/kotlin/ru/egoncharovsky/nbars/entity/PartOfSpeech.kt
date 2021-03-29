package ru.egoncharovsky.nbars.entity

sealed class PartOfSpeech(val label: String) {

    companion object {
        val labels = listOf(
            Noun.label,
            Adjective.label,
            Adverb.label,
            Numeric.label,
            Verb.label,
            Preposition.label,
            Participle.label,
            Interjection.label,
            Pronoun.label,
            Conjunction.label
        )
            .plus(Pronoun.SubType.values().map { it.label })
            .plus(Conjunction.SubType.values().map { it.label })

        fun byLabel(typeLabel: String, subTypeLabel: String? = null): PartOfSpeech {
            return when (typeLabel) {
                Noun.label -> Noun
                Adjective.label -> Adjective
                Adverb.label -> Adverb
                Numeric.label -> Numeric
                Verb.label -> Verb
                Preposition.label -> Preposition
                Participle.label -> Participle
                Interjection.label -> Interjection

                Pronoun.label -> {
                    val subType = subTypeLabel?.let { Pronoun.SubType.byLabel(it) }
                    Pronoun(subType)
                }
                Conjunction.label -> {
                    val subType = subTypeLabel?.let { Conjunction.SubType.byLabel(it) }
                    Conjunction(subType)
                }

                else -> throw IllegalArgumentException("Unknown label for type: '$typeLabel'")
            }
        }
    }
}

object Noun : PartOfSpeech("n")
object Adjective : PartOfSpeech("a")
object Adverb : PartOfSpeech("adv")
object Numeric : PartOfSpeech("num")
object Verb : PartOfSpeech("v")
object Preposition : PartOfSpeech("prep")
object Participle : PartOfSpeech("part")
object Interjection : PartOfSpeech("int")

data class Pronoun(val subType: SubType? = null) : PartOfSpeech(label) {

    enum class SubType(val label: String) {
        INDEFINITE("indef"),
        PERSONAL("pers"),
        POSSESSIVE("poss"),
        REFLEXIVE("refl"),
        RELATIVE("rel");

        companion object {
            fun byLabel(label: String) = values().find { it.label == label }
                ?: throw IllegalArgumentException("Unknown label for syb type: '$label'")
        }
    }

    companion object {
        const val label = "pron"
    }
}

data class Conjunction(val subType: SubType? = null) : PartOfSpeech(label) {

    enum class SubType(val label: String) {
        CORRELATIVE("corr"),
        COORDINATIVE("coord");

        companion object {
            fun byLabel(label: String) = SubType.values().find { it.label == label }
                ?: throw IllegalArgumentException("Unknown label for syb type: '$label'")
        }
    }

    companion object {
        const val label = "cj"
    }
}



