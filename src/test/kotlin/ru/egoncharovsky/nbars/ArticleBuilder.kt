package ru.egoncharovsky.nbars

import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.Homonym
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.Translation.Variant
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.PlainText
import ru.egoncharovsky.nbars.entity.text.Text
import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets

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
        partOfSpeech: String,
        applyParams: (HomonymBuilder) -> Unit
    ): HomonymsBuilder {
        homonyms.add(HomonymBuilder(transcription, partOfSpeech).also(applyParams).build())
        return this
    }

    fun build(): List<Homonym> = homonyms


}

class HomonymBuilder(
    private val transcription: String,
    private val partOfSpeech: String
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

    fun build(): Homonym = Homonym(transcription, partOfSpeech, translations)


}

class TranslationBuilder(
    private val remark: Text?,
    private val comment: Text?
) {
    private val variants = mutableListOf<Variant>()

    fun variant(meaning: String, applyParams: (VariantBuilder) -> Unit = {}): TranslationBuilder {
        variant(PlainText(replaceEscapedBrackets(meaning)), null, null, applyParams)
        return this
    }

    fun variant(
        meaning: Text,
        remark: Text? = null,
        comment: Text? = null,
        applyParams: (VariantBuilder) -> Unit = {}
    ): TranslationBuilder {
        variants.add(VariantBuilder(meaning, remark, comment).also(applyParams).build())
        return this
    }

    fun build(): Translation = Translation(variants, remark, comment)


}

class VariantBuilder(
    private val meaning: Text,
    private val remark: Text? = null,
    private val comment: Text? = null
) {
    private val examples = mutableListOf<Example>()

    fun example(text: String, lang: String, translation: String): VariantBuilder {
        example(text, lang, PlainText(replaceEscapedBrackets(translation)))
        return this
    }

    fun example(text: String, lang: String, translation: Text): VariantBuilder {
        examples.add(Example(ForeignText(replaceEscapedBrackets(text), lang), translation))
        return this
    }

    fun build(): Variant = Variant(meaning, remark, comment, examples)
}