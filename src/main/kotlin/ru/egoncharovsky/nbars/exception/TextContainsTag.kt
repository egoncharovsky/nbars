package ru.egoncharovsky.nbars.exception

import java.lang.Exception

class TextContainsTag(val text: String) : Exception("Text should not contain tag but it does: '$text'") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextContainsTag) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}