package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.article.section.WordArticleSection

data class WordArticle(
    override val headword: String,
    override val homonyms: List<List<WordArticleSection>>
) : Article<WordArticleSection>