package ru.egoncharovsky.nbars.entity.article

interface Article<S> {
    val headword: String
    val homonyms: List<List<S>>
}