package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.article.Article
import ru.egoncharovsky.nbars.entity.article.Homonym
import ru.egoncharovsky.nbars.entity.article.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class ArticleBuilder(private val keyword: String) {

    private val homonyms = mutableListOf<List<Either<Homonym, ReferenceToArticle>>>()

    fun homonyms(applyParams: (HomonymsBuilder) -> Unit): ArticleBuilder {
        homonyms.add(HomonymsBuilder().also(applyParams).build())
        return this
    }

    fun build(): Article = Article(keyword, homonyms)
}

class HomonymsBuilder {

    private val homonyms = mutableListOf<Either<Homonym, ReferenceToArticle>>()

    fun homonym(
        transcription: String,
        partOfSpeech: PartOfSpeech,
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (HomonymBuilder) -> Unit
    ): HomonymsBuilder {
        homonyms.add(
            Either.Left(
                HomonymBuilder(tr(transcription), partOfSpeech, remark, comment).also(applyParams).build()
            )
        )
        return this
    }

    fun reference(
        transcription: String,
        referenceOnHeadWord: String,
        grammaticalForm: GrammaticalForm? = null,
        applyParams: (ReferenceToArticleBuilder) -> Unit = {}
    ): HomonymsBuilder {
        homonyms.add(
            Either.Right(
                ReferenceToArticleBuilder(
                    tr(transcription),
                    st(referenceOnHeadWord),
                    grammaticalForm
                ).also(applyParams).build()
            )
        )
        return this
    }

    fun build(): List<Either<Homonym, ReferenceToArticle>> = homonyms
}

class HomonymBuilder(
    private val transcription: Transcription,
    private val partOfSpeech: PartOfSpeech,
    private val remark: Text? = null,
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

    fun build(): Homonym = Homonym(transcription, partOfSpeech, remark, comment, translations)
}