package ru.egoncharovsky.nbars.entity.translation

import ru.egoncharovsky.nbars.entity.text.Text

data class DirectTranslation(
    override val variants: List<Variant>,
    override val remark: Text? = null,
    override val comment: Text? = null
) : Translation