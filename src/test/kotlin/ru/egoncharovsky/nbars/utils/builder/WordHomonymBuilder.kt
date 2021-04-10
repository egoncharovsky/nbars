package ru.egoncharovsky.nbars.utils.builder

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.section.WordHomonym
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.SentenceHelper.stn
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr
import ru.egoncharovsky.nbars.utils.builder.part.Examples
import ru.egoncharovsky.nbars.utils.builder.part.Translations

class WordHomonymBuilder(
    val transcription: String,
    val partOfSpeech: PartOfSpeech,
    val remark: String? = null,
    val comment: String? = null
) : Translations<WordHomonymBuilder>, Builder<WordHomonym> {
    private val translations = mutableListOf<Translation>()
    private var idioms: List<Example>? = null

    fun idioms(applyParams: (IdiomsBuilder) -> Unit) {
        idioms = IdiomsBuilder().also(applyParams).build()
    }

    override fun add(translation: Translation) {
        translations.add(translation)
    }

    override fun builder(): WordHomonymBuilder = this

    override fun build(): WordHomonym =
        WordHomonym(tr(transcription), partOfSpeech, translations, stn(remark), stn(comment), idioms)
}

class IdiomsBuilder(): Builder<List<Example>>, Examples<IdiomsBuilder> {
    private val examples = mutableListOf<Example>()

    override fun add(example: Example) {
        examples.add(example)
    }

    override fun builder(): IdiomsBuilder = this

    override fun build(): List<Example> = examples
}