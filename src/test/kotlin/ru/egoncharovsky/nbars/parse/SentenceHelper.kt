package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.text.*

object SentenceHelper {
    fun st(vararg parts: Any): Sentence {
        val texts = parts.map {
            when (it) {
                is String -> pt(it)
                is Text -> it
                else -> throw IllegalArgumentException("Incorrect type for $it")
            }
        }
        return Sentence(texts)
    }

    fun pt(s: String) = PlainText(Text.replaceEscapedBrackets(s))
    fun ft(s: String, lang: String) = ForeignText(Text.replaceEscapedBrackets(s), lang)
    fun ab(s: String) = Abbreviation(s)
    fun rf(s: String) = Reference(s)
    fun tr(s: String) = Transcription(s)
}