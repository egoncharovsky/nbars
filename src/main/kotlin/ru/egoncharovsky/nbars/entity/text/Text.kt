package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.Regexes

interface Text {
    fun asPlain(): String

    companion object {
        fun requireNoTags(value: String) =
            require(!Regexes.squareBrackets.containsMatchIn(value)) { "Text should not contain tag but it does: $value" }

        fun replaceEscapedBrackets(value: String): String {
            return value
                .replace(Regexes.leftEscapedSquareBracket, "(")
                .replace(Regexes.rightEscapedSquareBracket, ")")
        }
    }
}