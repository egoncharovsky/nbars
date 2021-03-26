package ru.egoncharovsky.nbars.entity

data class Translation(
    val variants: List<TranslationVariant>,
    val remark: String?
) {
    override fun toString(): String = remark?.let { "$it " } ?: ""
}