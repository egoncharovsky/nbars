package ru.egoncharovsky.nbars.utils.builder.article

import ru.egoncharovsky.nbars.entity.PartOfSpeech
import ru.egoncharovsky.nbars.entity.article.WordArticle
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.article.section.WordArticleSection
import ru.egoncharovsky.nbars.utils.builder.Builder
import ru.egoncharovsky.nbars.utils.builder.WordHomonymBuilder
import ru.egoncharovsky.nbars.utils.builder.part.References

class WordArticleBuilder(
    private val headword: String
) : ArticleBuilder<WordArticle, WordArticleSection, WordArticleSectionsBuilder, WordArticleBuilder>() {
    override fun build(): WordArticle = WordArticle(headword, homonyms)

    override fun sectionsBuilder(): WordArticleSectionsBuilder = WordArticleSectionsBuilder()

    override fun builder(): WordArticleBuilder = this
}

class WordArticleSectionsBuilder : Builder<List<WordArticleSection>>, References<WordArticleSectionsBuilder> {

    private val sections = mutableListOf<WordArticleSection>()

    fun homonym(
        transcription: String,
        partOfSpeech: PartOfSpeech,
        remark: String? = null,
        comment: String? = null,
        applyParams: (WordHomonymBuilder) -> Unit,
    ): WordArticleSectionsBuilder {
        sections.add(WordHomonymBuilder(transcription, partOfSpeech, remark, comment).also(applyParams).build())
        return this
    }

    override fun add(reference: ReferenceToArticle) {
        sections.add(reference)
    }

    override fun builder(): WordArticleSectionsBuilder = this

    override fun build(): List<WordArticleSection> = sections
}