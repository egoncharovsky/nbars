package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.requireNoTags

data class ForeignText(
    val text: String,
    val language: String
) : TextPart {
    init {
        requireNoTags(text)
        requireNoTags(language)
    }

    override fun asPlain(): String = text
}