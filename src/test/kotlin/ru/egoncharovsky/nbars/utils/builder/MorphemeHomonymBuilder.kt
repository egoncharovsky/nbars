package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.section.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.SentenceHelper.stn
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr
import ru.egoncharovsky.nbars.utils.builder.part.Translations

class MorphemeHomonymBuilder(
    private val transcription: String,
    private val type: MorphemeType? = null,
    private val comment: String? = null
) : Translations<MorphemeHomonymBuilder>, Builder<MorphemeHomonym> {
    private val translations = mutableListOf<Translation>()

    override fun add(translation: Translation) {
        translations.add(translation)
    }

    override fun builder(): MorphemeHomonymBuilder = this

    override fun build() = MorphemeHomonym(tr(transcription), type, translations, stn(comment))
}