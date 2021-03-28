package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text

data class Translation(
    val variants: List<Variant>,
    val remark: Text? = null,
    val comment: Text? = null
) {

    data class Variant(
        val meaning: Text,
        val remark: Text? = null,
        val comment: Text? = null
    ) {
        override fun toString(): String =
            "${remark?.asPlain().orEmpty()} ${meaning.asPlain()} ${comment?.asPlain().orEmpty()}".trim()
    }
}