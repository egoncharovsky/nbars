package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.requireNoTags

data class Abbreviation(
    val short: String
) : Text {
    init {
        requireNoTags(short)
    }

    override fun asPlain(): String = short
}