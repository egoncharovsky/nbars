package ru.egoncharovsky.nbars.entity.text

data class Abbreviation(
    val short: String
) : Text {
    init {
        requireNoTags(short)
    }

    override fun asPlain(): String = short
    override fun toString(): String = "Abbreviation(${asPlain()})"

}