package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.ReferenceType
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription

data class ReferenceArticle(
    override val headword: String,

    val transcription: Transcription,
    val referenceType: ReferenceType? = null,
    val toHeadWord: Text
) : DictionaryArticle