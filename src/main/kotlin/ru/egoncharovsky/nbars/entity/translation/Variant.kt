package ru.egoncharovsky.nbars.entity.translation

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.Text

data class Variant(
    val meaning: Text,
    val examples: List<Example> = listOf(),
    val remark: Text? = null,
    val comment: Text? = null
)