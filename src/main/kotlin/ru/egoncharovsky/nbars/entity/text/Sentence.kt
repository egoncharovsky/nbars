package ru.egoncharovsky.nbars.entity.text

data class Sentence(
    val parts: List<Text>
) : Text {

    constructor(vararg parts: Text) : this(parts.asList())

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }
    override fun toString(): String  = asPlain()

    companion object {
        fun pt(s: String) = PlainText(s)
        fun ft(s: String, lang: String) = ForeignText(s, lang)
        fun ab(s: String) = Abbreviation(s)
    }
}