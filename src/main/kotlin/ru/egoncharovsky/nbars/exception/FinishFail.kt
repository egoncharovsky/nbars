package ru.egoncharovsky.nbars.exception

class FinishFail(
    val expected: String,
    val raw: String
) : Exception("Finish: expected $expected but not: '$raw'") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FinishFail) return false

        if (expected != other.expected) return false

        return true
    }

    override fun hashCode(): Int {
        return expected.hashCode()
    }
}