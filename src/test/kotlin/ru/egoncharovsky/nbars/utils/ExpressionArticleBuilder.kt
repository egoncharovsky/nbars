package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.translation.DirectTranslation
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class ExpressionArticleBuilder(
    private val headword: String,
    private val transcription: String,
    private val expressionType: ExpressionType? = null,
    private val comment: Text? = null
) {
    private val translations = mutableListOf<DirectTranslation>()

    fun translation(
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationBuilder) -> Unit
    ): ExpressionArticleBuilder {
        translations.add(TranslationBuilder(remark, comment).also(applyParams).build())
        return this
    }

    fun build(): ExpressionArticle = ExpressionArticle(headword, tr(transcription), expressionType, comment, translations)
}