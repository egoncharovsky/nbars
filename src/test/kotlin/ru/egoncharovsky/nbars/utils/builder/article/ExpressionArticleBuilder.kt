package ru.egoncharovsky.nbars.utils.builder.article

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.article.section.ExpressionArticleSection
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.utils.builder.Builder
import ru.egoncharovsky.nbars.utils.builder.ExpressionHomonymBuilder
import ru.egoncharovsky.nbars.utils.builder.part.References

class ExpressionArticleBuilder(
    private val headword: String
) : ArticleBuilder<ExpressionArticle, ExpressionArticleSection, ExpressionSectionsBuilder, ExpressionArticleBuilder>() {

    override fun build(): ExpressionArticle = ExpressionArticle(headword, homonyms)

    override fun sectionsBuilder(): ExpressionSectionsBuilder = ExpressionSectionsBuilder()

    override fun builder(): ExpressionArticleBuilder = this
}

class ExpressionSectionsBuilder : Builder<List<ExpressionArticleSection>>, References<ExpressionSectionsBuilder> {
    private val sections = mutableListOf<ExpressionArticleSection>()

    override fun builder(): ExpressionSectionsBuilder = this

    override fun add(reference: ReferenceToArticle) {
        sections.add(reference)
    }

    fun homonym(
        transcription: String,
        expressionType: ExpressionType? = null,
        remark: String? = null,
        comment: String? = null,
        applyParams: (ExpressionHomonymBuilder) -> Unit
    ): ExpressionSectionsBuilder {
        sections.add(
            ExpressionHomonymBuilder(transcription, expressionType, remark, comment).also(applyParams).build()
        )
        return this
    }

    override fun build(): List<ExpressionArticleSection> = sections
}