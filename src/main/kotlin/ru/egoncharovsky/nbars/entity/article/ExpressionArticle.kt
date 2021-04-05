package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.translation.DirectTranslation
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

data class ExpressionArticle(
    override val headword: String,

    val transcription: Transcription,
    val expressionType: ExpressionType?,
    val comment: Text? = null,
    val translations: List<DirectTranslation>
) : DictionaryArticle
