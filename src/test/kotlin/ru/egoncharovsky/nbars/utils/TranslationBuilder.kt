package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.utils.SentenceHelper.st

class TranslationBuilder(
    private val remark: Text?,
    private val comment: Text?
) {
    private val variants = mutableListOf<Translation.Variant>()

    fun variant(
        meaning: String,
        remark: String? = null,
        comment: String? = null,
        applyParams: (TranslationVariantBuilder) -> Unit = {}
    ): TranslationBuilder {
        return variant(st(meaning), remark?.let { st(it) }, comment?.let { st(it) }, applyParams)
    }

    fun variant(
        meaning: Text,
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationVariantBuilder) -> Unit = {}
    ): TranslationBuilder {
        variants.add(TranslationVariantBuilder(meaning, remark, comment).also(applyParams).build())
        return this
    }

    fun build(): Translation = Translation(variants, remark, comment)


}

class TranslationVariantBuilder(
    private val meaning: Text,
    private val remark: Text? = null,
    private val comment: Text? = null
) {
    private val examples = mutableListOf<Example>()

    fun example(text: String, lang: String, translation: String): TranslationVariantBuilder {
        example(text, lang, st(translation))
        return this
    }

    fun example(text: String, lang: String, translation: Text): TranslationVariantBuilder {
        examples.add(Example(ForeignText(Text.normalize(text), lang), translation))
        return this
    }

    fun build(): Translation.Variant = Translation.Variant(meaning, remark, comment, examples)
}