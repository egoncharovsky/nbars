package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Translation

data class MorphemeHomonym(
    val transcription: Transcription,
    val type: MorphemeType?,
    val translation: Translation
)