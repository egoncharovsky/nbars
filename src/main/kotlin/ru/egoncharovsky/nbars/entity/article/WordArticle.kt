package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.WordHomonym

data class WordArticle(
    override val headword: String,
    val homonyms: List<List<Either<WordHomonym, ReferenceToArticle>>>
) : DictionaryArticle