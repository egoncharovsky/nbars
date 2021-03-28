package ru.egoncharovsky.nbars.entity.text

data class PlainText(
    val text: String
) : Text {
    init {
        requireNoTags(text)
    }

    override fun asPlain(): String = text
}
