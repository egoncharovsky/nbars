package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.article.section.ArticleSection

data class WordArticle(
    override val headword: String,
    val homonyms: List<List<ArticleSection>>
) : DictionaryArticle