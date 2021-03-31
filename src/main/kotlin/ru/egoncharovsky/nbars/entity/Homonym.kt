package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

data class Homonym(
    val transcription: Transcription,
    val partOfSpeech: PartOfSpeech,
    val comment: Text? = null,
    val translations: List<Translation>
)