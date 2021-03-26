package ru.egoncharovsky.nbars.entity

data class LexGramHomonym(
    val transcription: String,
    val partOfSpeech: String,
    val translations: List<Translation>
) {
    override fun toString(): String = "[$transcription] $partOfSpeech"
}