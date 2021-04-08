package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.article.section.MorphemeArticleSection

data class MorphemeArticle(
    override val headword: String,
    override val homonyms: List<List<MorphemeArticleSection>>
) : Article<MorphemeArticleSection>
