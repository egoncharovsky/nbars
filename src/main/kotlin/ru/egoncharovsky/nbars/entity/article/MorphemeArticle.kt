package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.MorphemeHomonym

data class MorphemeArticle(
    override val headword: String,

    val homonyms: Either<List<MorphemeHomonym>, ReferenceToArticle>
) : DictionaryArticle
