package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.article.section.ExpressionHomonym
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.SentenceHelper.stn
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr
import ru.egoncharovsky.nbars.utils.builder.part.Idioms
import ru.egoncharovsky.nbars.utils.builder.part.Translations

class ExpressionHomonymBuilder(
    private val transcription: String,
    private val expressionType: ExpressionType? = null,
    private val remark: String? = null,
    private val comment: String? = null
) : Builder<ExpressionHomonym>, Translations<ExpressionHomonymBuilder>, Idioms<ExpressionHomonymBuilder> {
    private val translations = mutableListOf<Translation>()
    private var idioms: MutableList<Example>? = null

    override fun add(translation: Translation) {
        translations.add(translation)
    }

    override fun add(example: Example) {
        if (idioms == null) idioms = mutableListOf()
        idioms!!.add(example)
    }

    override fun builder(): ExpressionHomonymBuilder = this
    override fun build() =
        ExpressionHomonym(tr(transcription), expressionType, translations, stn(remark), stn(comment), idioms)
}