package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.translation.Variant

class VariantBuilder(
    private val meaning: Text,
    private val remark: Text? = null,
    private val comment: Text? = null,
) {
    private val examples = mutableListOf<Example>()

    fun example(text: String, lang: String, translation: String): VariantBuilder {
        example(text, lang, SentenceHelper.st(translation))
        return this
    }

    fun example(text: String, lang: String, translation: Text): VariantBuilder {
        examples.add(Example(ForeignText(Text.normalize(text), lang), translation))
        return this
    }

    fun build(): Variant = Variant(meaning, examples, remark, comment)
}