package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.utils.SentenceHelper.stn
import ru.egoncharovsky.nbars.utils.builder.part.Examples

class ReferenceToArticleBuilder(
    private val transcription: Transcription,
    private val referenceOnHeadWord: Text,
    private val grammaticalForm: GrammaticalForm? = null,
    private val comment: String? = null
) : Examples<ReferenceToArticleBuilder>, Builder<ReferenceToArticle> {
    private val examples = mutableListOf<Example>()

    override fun add(example: Example) {
        examples.add(example)
    }

    override fun builder(): ReferenceToArticleBuilder = this

    override fun build() =
        ReferenceToArticle(transcription, grammaticalForm, referenceOnHeadWord, examples, stn(comment))
}