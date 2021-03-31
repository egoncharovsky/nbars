package ru.egoncharovsky.nbars.exception

class IntersectedRangesFound(val raw: String, ranges: String) : Exception("Intersected tag ranges found in $raw: $ranges") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IntersectedRangesFound) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}