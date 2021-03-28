package ru.egoncharovsky.nbars

import ru.egoncharovsky.nbars.entity.Article
import ru.egoncharovsky.nbars.entity.Homonym
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.Translation.Variant
import ru.egoncharovsky.nbars.entity.text.Text

class ArticleBuilder(private val keyword: String) {

    private val homonyms = mutableListOf<List<Homonym>>()

    fun homonyms(applyParams: (HomonymsBuilder) -> Unit): ArticleBuilder {
        homonyms.add(HomonymsBuilder().also(applyParams).build())
        return this
    }

    fun build(): Article = Article(keyword, homonyms)

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

            class TranslationBuilder(
                private val remark: Text?,
                private val comment: Text?
            ) {
                private val variants = mutableListOf<Variant>()

                fun variant(meaning: Text, remark: Text? = null, comment: Text? = null): TranslationBuilder {
                    variants.add(Variant(meaning, remark, comment))
                    return this
                }

                fun build(): Translation = Translation(variants, remark, comment)
            }
        }
    }
}