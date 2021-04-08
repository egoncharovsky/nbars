package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.article.section.ExpressionArticleSection

data class ExpressionArticle(
    override val headword: String,
    override val homonyms: List<List<ExpressionArticleSection>>
) : Article<ExpressionArticleSection>
