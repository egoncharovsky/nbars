package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.entity.translation.Variant
import ru.egoncharovsky.nbars.utils.SentenceHelper.st

class TranslationBuilder(
    private val remark: Text?,
    private val comment: Text?,
) {
    private val variants = mutableListOf<Variant>()

    fun variant(
        meaning: String,
        remark: String? = null,
        comment: String? = null,
        applyParams: (VariantBuilder) -> Unit = {},
    ): TranslationBuilder {
        return variant(st(meaning), remark?.let { st(it) }, comment?.let { st(it) }, applyParams)
    }

    fun variant(
        meaning: Text,
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (VariantBuilder) -> Unit = {},
    ): TranslationBuilder {
        variants.add(VariantBuilder(meaning, remark, comment).also(applyParams).build())
        return this
    }

    fun build(): Translation = Translation(variants, remark, comment)


}

