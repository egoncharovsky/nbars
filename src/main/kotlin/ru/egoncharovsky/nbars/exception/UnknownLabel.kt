package ru.egoncharovsky.nbars.exception

class UnknownLabel(val forS: String, val label: String) : Exception("Unknown label for $forS: '$label'") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnknownLabel) return false

        if (forS != other.forS) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = forS.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }
}