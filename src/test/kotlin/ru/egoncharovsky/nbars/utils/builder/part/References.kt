package ru.egoncharovsky.nbars.utils.builder.part

import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.utils.SentenceHelper
import ru.egoncharovsky.nbars.utils.builder.ReferenceToArticleBuilder

interface References<B : References<B>> {

    fun reference(
        transcription: String,
        referenceOnHeadWord: String,
        grammaticalForm: GrammaticalForm? = null,
        applyParams: (ReferenceToArticleBuilder) -> Unit = {},
    ): B {
        add(
            ReferenceToArticleBuilder(
                SentenceHelper.tr(transcription),
                SentenceHelper.st(referenceOnHeadWord),
                grammaticalForm
            ).also(applyParams).build()
        )
        return builder()
    }

    fun add(reference: ReferenceToArticle)

    fun builder(): B
}