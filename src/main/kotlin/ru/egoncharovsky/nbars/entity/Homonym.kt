package ru.egoncharovsky.nbars.entity

data class Homonym(
    val lexGramHomonyms: List<LexGramHomonym>
) {
    override fun toString(): String = "Homonym"
}