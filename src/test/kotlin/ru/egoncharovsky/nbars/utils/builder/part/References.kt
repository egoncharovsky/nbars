package ru.egoncharovsky.nbars.utils.builder.part

import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.utils.SentenceHelper
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.trn
import ru.egoncharovsky.nbars.utils.builder.ReferenceToArticleBuilder

interface References<B : References<B>> {

    fun reference(
        transcription: String? = null,
        referenceOnHeadWord: String,
        grammaticalForm: GrammaticalForm? = null,
        comment: String? = null,
        applyParams: (ReferenceToArticleBuilder) -> Unit = {},
    ): B {
        add(
            ReferenceToArticleBuilder(
                trn(transcription),
                st(referenceOnHeadWord),
                grammaticalForm,
                comment
            ).also(applyParams).build()
        )
        return builder()
    }

    fun add(reference: ReferenceToArticle)

    fun builder(): B
}