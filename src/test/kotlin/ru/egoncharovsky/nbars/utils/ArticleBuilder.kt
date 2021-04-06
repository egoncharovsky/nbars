package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.WordArticle
import ru.egoncharovsky.nbars.entity.article.section.ArticleSection
import ru.egoncharovsky.nbars.entity.article.section.WordHomonym
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.DirectTranslation
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class ArticleBuilder(private val keyword: String) {

    private val homonyms = mutableListOf<List<ArticleSection>>()

    fun homonyms(applyParams: (SectionsBuilder) -> Unit): ArticleBuilder {
        homonyms.add(SectionsBuilder().also(applyParams).build())
        return this
    }

    fun build(): WordArticle = WordArticle(keyword, homonyms)
}

class SectionsBuilder {

    private val homonyms = mutableListOf<ArticleSection>()

    fun homonym(
        transcription: String,
        partOfSpeech: PartOfSpeech,
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (HomonymBuilder) -> Unit,
    ): SectionsBuilder {
        homonyms.add(HomonymBuilder(tr(transcription), partOfSpeech, remark, comment).also(applyParams).build())
        return this
    }

    fun reference(
        transcription: String,
        referenceOnHeadWord: String,
        grammaticalForm: GrammaticalForm? = null,
        applyParams: (ReferenceToArticleBuilder) -> Unit = {},
    ): SectionsBuilder {
        homonyms.add(ReferenceToArticleBuilder(
            tr(transcription),
            st(referenceOnHeadWord),
            grammaticalForm
        ).also(applyParams).build())
        return this
    }

    fun build(): List<ArticleSection> = homonyms
}

class HomonymBuilder(
    private val transcription: Transcription,
    private val partOfSpeech: PartOfSpeech,
    private val remark: Text? = null,
    private val comment: Text? = null,
) {
    private val translations = mutableListOf<DirectTranslation>()

    fun translation(
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationBuilder) -> Unit,
    ): HomonymBuilder {
        translations.add(TranslationBuilder(remark, comment).also(applyParams).build())
        return this
    }

    fun build(): WordHomonym = WordHomonym(transcription, partOfSpeech, remark, comment, translations)
}