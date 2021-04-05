package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.DirectTranslation
import ru.egoncharovsky.nbars.entity.translation.Translation

data class WordHomonym(
    val transcription: Transcription,
    val partOfSpeech: PartOfSpeech,
    val remark: Text? = null,
    val comment: Text? = null,
    val translations: List<Translation>
)