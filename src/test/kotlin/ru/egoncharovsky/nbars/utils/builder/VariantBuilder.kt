package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.translation.Variant
import ru.egoncharovsky.nbars.utils.SentenceHelper.st

class VariantBuilder(
    private val meaning: Text,
    private val remark: Text? = null,
    private val comment: Text? = null,
) {
    private val examples = mutableListOf<Example>()

    fun example(text: ForeignText, translation: String): VariantBuilder {
        examples.add(Example(text, st(translation)))
        return this
    }

    fun example(text: String, lang: String, translation: String): VariantBuilder {
        example(text, lang, st(translation))
        return this
    }

    fun example(text: String, lang: String, translation: Text): VariantBuilder {
        examples.add(Example(ForeignText(Text.normalize(text), lang), translation))
        return this
    }

    fun build(): Variant = Variant(meaning, examples, remark, comment)
}