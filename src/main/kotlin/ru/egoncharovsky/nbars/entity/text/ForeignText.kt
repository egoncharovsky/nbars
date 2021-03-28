package ru.egoncharovsky.nbars.entity.text

data class ForeignText(
    val text: String,
    val language: String
) : Text {
    init {
        requireNoTags(text)
        requireNoTags(language)
    }

    override fun asPlain(): String = text
}