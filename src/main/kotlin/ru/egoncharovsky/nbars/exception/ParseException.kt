package ru.egoncharovsky.nbars.exception

import ru.egoncharovsky.nbars.parse.RawPart
import java.lang.Exception

class ParseException(val step: String, val details: String, val raw: RawPart) : Exception("Can't parse $step: $details in '$raw'") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParseException) return false

        if (details != other.details) return false

        return true
    }

    override fun hashCode(): Int {
        return details.hashCode()
    }
}
