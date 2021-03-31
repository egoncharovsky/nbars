package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.squareBrackets
import ru.egoncharovsky.nbars.exception.TextContainsTag

interface Text {
    fun asPlain(): String

    companion object {
        fun requireNoTags(value: String) {
            if (squareBrackets.containsMatchIn(value)) throw TextContainsTag(value)
        }

        fun replaceEscapedBrackets(value: String): String {
            return value
                .replace(Regexes.leftEscapedSquareBracket, "(")
                .replace(Regexes.rightEscapedSquareBracket, ")")
        }
    }
}