package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.MorphemeMeaning
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.utils.SentenceHelper.ft
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

class MorphemeBuilder(private val keyword: String) {
    private var homonyms: Either<MutableList<MorphemeMeaning>, ReferenceToArticle>? = null

    fun homonym(
        transcription: String,
        type: MorphemeType? = null,
        comment: String? = null,
        applyParams: (MorphemeMeaningBuilder) -> Unit
    ): MorphemeBuilder {
        if (homonyms == null) homonyms = Either.Left(mutableListOf())
        (homonyms as Either.Left).value.add(
            MorphemeMeaningBuilder(transcription, type, comment).also(applyParams).build()
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

class MorphemeMeaningBuilder(
    private val transcription: String,
    private val type: MorphemeType? = null,
    private val comment: String? = null
) {
    private val variants = mutableListOf<MorphemeMeaning.Variant>()

    fun variant(
        meaning: String,
        applyParams: (MeaningVariantBuilder) -> Unit = {}
    ): MorphemeMeaningBuilder {
        variants.add(MeaningVariantBuilder(st(meaning)).also(applyParams).build())
        return this
    }

    fun build(): MorphemeMeaning = MorphemeMeaning(tr(transcription), variants, type, comment?.let { st(it) })
}

class MeaningVariantBuilder(
    private val meaning: Text,
) {
    private val examples = mutableListOf<Example>()

    fun example(text: String, lang: String, translation: String): MeaningVariantBuilder {
        examples.add(Example(ft(text, lang), st(translation)))
        return this
    }

    fun build(): MorphemeMeaning.Variant = MorphemeMeaning.Variant(meaning, examples)
}

