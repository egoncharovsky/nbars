package ru.egoncharovsky.nbars.entity

data class Article(
    val headword: String,
    val homonyms: List<List<Homonym>>
)