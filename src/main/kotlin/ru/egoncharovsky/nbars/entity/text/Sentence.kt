package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets

data class Sentence(
    val parts: List<Text>
) : Text {

    constructor(vararg parts: Text) : this(parts.asList())

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }

    companion object {
        fun st(vararg parts: Text) = Sentence(*parts)
        fun pt(s: String) = PlainText(replaceEscapedBrackets(s))
        fun ft(s: String, lang: String) = ForeignText(replaceEscapedBrackets(s), lang)
        fun ab(s: String) = Abbreviation(s)
        fun rf(s: String) = Reference(s)
    }
}