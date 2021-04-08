package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.article.section.ExpressionHomonym
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.SentenceHelper.stn
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr
import ru.egoncharovsky.nbars.utils.builder.part.Translations

class ExpressionHomonymBuilder(
    private val transcription: String,
    private val expressionType: ExpressionType? = null,
    private val remark: String? = null,
    private val comment: String? = null
) : Translations<ExpressionHomonymBuilder>, Builder<ExpressionHomonym> {
    private val translations = mutableListOf<Translation>()

    override fun add(translation: Translation) {
        translations.add(translation)
    }

    override fun builder(): ExpressionHomonymBuilder = this

    override fun build() = ExpressionHomonym(tr(transcription), expressionType, translations, stn(remark), stn(comment))
}