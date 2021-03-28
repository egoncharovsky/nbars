package ru.egoncharovsky.nbars.entity

data class Homonym(
    val transcription: String,
    val partOfSpeech: String,
    val translations: List<Translation>
)