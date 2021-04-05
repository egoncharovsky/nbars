package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Meaning

data class MorphemeHomonym(
    val transcription: Transcription,
    val type: MorphemeType?,
    val meaning: Meaning
)