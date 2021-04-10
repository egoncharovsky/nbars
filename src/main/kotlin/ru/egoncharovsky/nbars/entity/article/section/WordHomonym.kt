package ru.egoncharovsky.nbars.entity.article.section

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Translation

data class WordHomonym(//lex. gram. homonym
    val transcription: Transcription,
    val partOfSpeech: PartOfSpeech,
    val translations: List<Translation>,
    val remark: Text? = null,
    val comment: Text? = null,
    val idioms: List<Example>? = null
) : WordArticleSection