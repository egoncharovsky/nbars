package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.Homonym

data class Article(
    override val headword: String,
    val homonyms: List<List<Homonym>>
) : DictionaryArticle