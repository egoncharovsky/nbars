package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

data class MorphemeMeaning(
    val transcription: Transcription,
    val variants: List<Variant>,
    val type: MorphemeType?,
    val comment: Text? = null
) {

    data class Variant(
        val meaning: Text,
        val examples: List<Example> = listOf()
    )
}