package ru.egoncharovsky.nbars.entity

data class TranslationVariant(
    val meaning: String,
    val remark: String?
) {
    override fun toString(): String = "${remark?.let { "$it " } ?: ""}$meaning"
}