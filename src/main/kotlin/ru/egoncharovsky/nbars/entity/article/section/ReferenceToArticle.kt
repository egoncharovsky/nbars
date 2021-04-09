package ru.egoncharovsky.nbars.entity.article.section

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

data class ReferenceToArticle(
    val transcription: Transcription?,
    val grammaticalForm: GrammaticalForm? = null,
    val referenceOnHeadWord: Text,
    val examples: List<Example> = listOf(),
    val comment: Text? = null
) : WordArticleSection, MorphemeArticleSection, ExpressionArticleSection