package ru.egoncharovsky.nbars.entity.translation

import ru.egoncharovsky.nbars.entity.text.Text

data class Translation (
    val variants: List<Variant>,
    val remark: Text? = null,
    val comment: Text? = null
)