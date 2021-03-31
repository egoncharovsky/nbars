package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.entity.text.Text.Companion.requireNoTags

data class Reference(
    val word: String
): Text {
    init {
        requireNoTags(word)
    }

    override fun asPlain(): String = word
}
