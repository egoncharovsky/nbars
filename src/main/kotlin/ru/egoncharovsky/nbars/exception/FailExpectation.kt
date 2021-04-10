package ru.egoncharovsky.nbars.exception

import ru.egoncharovsky.nbars.parse.RawPart

class FailExpectation private constructor(
    val expectation: String,
    val raw: Any,
    val byRegex: Regex,
    val butWas: Any,
    val details: Any
) : Exception("$expectation but was $butWas by '$byRegex' in '$raw': $details") {
    constructor(
        expectation: String,
        raw: String,
        byRegex: Regex,
        butWas: Any,
        details: Any
    ) : this(expectation, raw as Any, byRegex, butWas, details)

    constructor(
        expectation: String,
        raw: RawPart,
        byRegex: Regex,
        butWas: Any,
        details: Any
    ) : this(expectation, raw as Any, byRegex, butWas, details)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FailExpectation) return false

        if (expectation != other.expectation) return false
        if (byRegex != other.byRegex) return false
        if (butWas != other.butWas) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expectation.hashCode()
        result = 31 * result + byRegex.hashCode()
        result = 31 * result + butWas.hashCode()
        return result
    }
}