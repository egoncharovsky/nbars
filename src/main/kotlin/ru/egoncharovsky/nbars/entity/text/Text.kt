package ru.egoncharovsky.nbars.entity.text

import ru.egoncharovsky.nbars.Regexes

interface Text {
    fun asPlain(): String

    fun requireNoTags(value: String) =
        require(!Regexes.squareBrackets.containsMatchIn(value)) { "Text should not contain tag but it does: $value" }
}