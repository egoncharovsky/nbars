package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.*
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class ArticleBuilder(private val keyword: String) {

    private val homonyms = mutableListOf<List<Homonym>>()

    fun homonyms(applyParams: (HomonymsBuilder) -> Unit): ArticleBuilder {
        homonyms.add(HomonymsBuilder().also(applyParams).build())
        return this
    }

    fun build(): Article = Article(keyword, homonyms)
}

class HomonymsBuilder {

    private val homonyms = mutableListOf<Homonym>()

    fun homonym(
        transcription: String,
        partOfSpeech: PartOfSpeech,
        comment: Text? = null,
        applyParams: (HomonymBuilder) -> Unit
    ): HomonymsBuilder {
        homonyms.add(HomonymBuilder(tr(transcription), partOfSpeech, comment).also(applyParams).build())
        return this
    }

    fun build(): List<Homonym> = homonyms


}

class HomonymBuilder(
    private val transcription: Transcription,
    private val partOfSpeech: PartOfSpeech,
    private val comment: Text? = null
) {
    private val translations = mutableListOf<Translation>()

    fun translation(
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationBuilder) -> Unit
    ): HomonymBuilder {
        translations.add(TranslationBuilder(remark, comment).also(applyParams).build())
        return this
    }

    fun build(): Homonym = Homonym(transcription, partOfSpeech, comment, translations)
}