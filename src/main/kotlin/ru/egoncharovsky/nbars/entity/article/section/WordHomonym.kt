package ru.egoncharovsky.nbars.entity.article.section

import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Translation

data class WordHomonym(
    val transcription: Transcription,
    val partOfSpeech: PartOfSpeech,
    val remark: Text? = null,
    val comment: Text? = null,
    val translations: List<Translation>
) : ArticleSection