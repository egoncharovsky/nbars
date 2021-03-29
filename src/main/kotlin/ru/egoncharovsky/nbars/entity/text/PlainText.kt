package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.requireNoTags

data class PlainText(
    val text: String
) : Text {
    init {
        requireNoTags(text)
    }

    override fun asPlain(): String = text
}
