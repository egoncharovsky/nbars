package ru.egoncharovsky.nbars.utils.builder.article

import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.section.MorphemeArticleSection
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.utils.builder.Builder
import ru.egoncharovsky.nbars.utils.builder.MorphemeHomonymBuilder
import ru.egoncharovsky.nbars.utils.builder.part.References

class MorphemeArticleBuilder(
    private val headword: String
) : ArticleBuilder<MorphemeArticle, MorphemeArticleSection, MorphemeArticleSectionsBuilder, MorphemeArticleBuilder>() {
    override fun sectionsBuilder(): MorphemeArticleSectionsBuilder = MorphemeArticleSectionsBuilder()

    override fun builder(): MorphemeArticleBuilder = this

    override fun build(): MorphemeArticle = MorphemeArticle(headword, homonyms)
}

class MorphemeArticleSectionsBuilder : Builder<List<MorphemeArticleSection>>,
    References<MorphemeArticleSectionsBuilder> {
    private val sections = mutableListOf<MorphemeArticleSection>()

    fun homonym(
        transcription: String,
        type: MorphemeType? = null,
        comment: String? = null,
        applyParams: (MorphemeHomonymBuilder) -> Unit
    ): MorphemeArticleSectionsBuilder {
        sections.add(MorphemeHomonymBuilder(transcription, type, comment).also(applyParams).build())
        return this
    }

    override fun build(): List<MorphemeArticleSection> = sections

    override fun add(reference: ReferenceToArticle) {
        sections.add(reference)
    }

    override fun builder(): MorphemeArticleSectionsBuilder = this
}
