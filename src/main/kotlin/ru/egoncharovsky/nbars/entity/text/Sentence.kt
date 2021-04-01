package ru.egoncharovsky.nbars.entity.text

data class Sentence(
    val parts: List<Text>
) : Text {

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }
}