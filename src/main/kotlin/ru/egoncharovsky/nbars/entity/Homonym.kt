package ru.egoncharovsky.nbars.entity

data class Homonym(
    val transcription: String,
    val partOfSpeech: PartOfSpeech,
    val translations: List<Translation>
)