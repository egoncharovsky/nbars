package ru.egoncharovsky.nbars.entity.translation

import ru.egoncharovsky.nbars.entity.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.text.Text

data class Meaning(
    override val variants: List<Variant>,
    override val comment: Text? = null
) : Translation {
    override val remark: Text?
        get() = null
}