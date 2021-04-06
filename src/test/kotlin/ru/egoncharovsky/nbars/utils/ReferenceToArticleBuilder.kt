package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

class ReferenceToArticleBuilder(
    private val transcription: Transcription,
    private val referenceOnHeadWord: Text,
    private val grammaticalForm: GrammaticalForm? = null
) {
    private val examples = mutableListOf<Example>()

    fun example(text: String, lang: String, translation: String): ReferenceToArticleBuilder {
        examples.add(Example(ForeignText(Text.normalize(text), lang), SentenceHelper.st(translation)))
        return this
    }

    fun build() = ReferenceToArticle(transcription, grammaticalForm, referenceOnHeadWord, examples)
}