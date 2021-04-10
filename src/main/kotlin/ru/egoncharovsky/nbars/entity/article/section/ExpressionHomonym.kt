package ru.egoncharovsky.nbars.entity.article.section

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Translation

data class ExpressionHomonym(
    val transcription: Transcription,
    val expressionType: ExpressionType?,
    val translations: List<Translation>,
    val remark: Text? = null,
    val comment: Text? = null,
    val idioms: List<Example>?
) : ExpressionArticleSection