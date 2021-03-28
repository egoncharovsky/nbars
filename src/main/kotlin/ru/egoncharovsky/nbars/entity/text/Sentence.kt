package ru.egoncharovsky.nbars.entity.text

data class Sentence(
    val parts: List<Text>
) : Text {

    constructor(vararg parts: Text) : this(parts.asList())

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }

    companion object {
        fun st(vararg parts: Text) = Sentence(*parts)
        fun pt(s: String) = PlainText(s)
        fun ft(s: String, lang: String) = ForeignText(s, lang)
        fun ab(s: String) = Abbreviation(s)
    }
}