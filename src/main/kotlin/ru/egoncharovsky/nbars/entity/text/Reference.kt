package ru.egoncharovsky.nbars.entity.text

data class Reference(
    val word: String
): Text {
    override fun asPlain(): String = word
}
