package ru.egoncharovsky.nbars.exception

class FailExpectation(
    val expectation: String,
    val raw: String,
    val byRegex: Regex,
    val butWas: Any,
    val details: Any
) : Exception("$expectation but was $butWas by '$byRegex' in '$raw': $details") {
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