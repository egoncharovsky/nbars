package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.requireNoTags

data class Transcription(
    val value: String
) : TextPart {
    init {
        requireNoTags(value)
    }

    override fun asPlain(): String = "[$value]"
}