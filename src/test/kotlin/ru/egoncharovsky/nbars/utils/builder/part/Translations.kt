package ru.egoncharovsky.nbars.utils.builder.part

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.builder.TranslationBuilder

interface Translations<B : Translations<B>> {

    fun translation(
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationBuilder) -> Unit
    ): B {
        add(TranslationBuilder(remark, comment).also(applyParams).build())
        return builder()
    }

    fun add(translation: Translation)

    fun builder(): B
}