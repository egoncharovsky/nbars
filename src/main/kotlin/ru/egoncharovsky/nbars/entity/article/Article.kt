package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.Either

data class Article(
    override val headword: String,
    val homonyms: List<List<Either<Homonym, ReferenceToArticle>>>
) : DictionaryArticle