package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets

data class Sentence(
    val parts: List<Text>
) : Text {

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }
}