package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class MorphemeBuilder(private val keyword: String) {
    private var homonyms: Either<MutableList<MorphemeHomonym>, ReferenceToArticle>? = null

    fun homonym(
        transcription: String,
        type: MorphemeType? = null,
        comment: String? = null,
        applyParams: (MorphemeHomonymBuilder) -> Unit
    ): MorphemeBuilder {
        if (homonyms == null) homonyms = Either.Left(mutableListOf())
        (homonyms as Either.Left).value.add(
            MorphemeHomonymBuilder(transcription, type, comment).also(applyParams).build()
        )
        return this
    }

    fun reference(
        transcription: String,
        referenceOnHeadWord: String,
        grammaticalForm: GrammaticalForm? = null,
        applyParams: (ReferenceToArticleBuilder) -> Unit = {}
    ): MorphemeArticle {
        if (homonyms != null) IllegalArgumentException("Already init as homonym")
        homonyms = Either.Right(
            ReferenceToArticleBuilder(
                tr(transcription),
                st(referenceOnHeadWord),
                grammaticalForm
            ).also(applyParams).build()
        )
        return build()
    }

    fun build(): MorphemeArticle = MorphemeArticle(keyword, homonyms!!)
}

class MorphemeHomonymBuilder(
    private val transcription: String,
    private val type: MorphemeType? = null,
    private val comment: String? = null
) {
    private val translations = mutableListOf<Translation>()

    fun translation(
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (TranslationBuilder) -> Unit,
    ): MorphemeHomonymBuilder {
        translations.add(TranslationBuilder(remark, comment).also(applyParams).build())
        return this
    }

    fun build(): MorphemeHomonym = MorphemeHomonym(tr(transcription), type, translations, comment?.let { st(it) })
}

