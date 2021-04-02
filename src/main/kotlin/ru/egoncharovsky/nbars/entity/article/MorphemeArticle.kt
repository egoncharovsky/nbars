package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.entity.MorphemeMeaning

data class MorphemeArticle(
    override val headword: String,

    val homonyms: Either<List<MorphemeMeaning>, ReferenceToArticle>
) : DictionaryArticle
