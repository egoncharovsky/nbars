package ru.egoncharovsky.nbars.exception

class EmptyRaw(val operation: String) : Exception("Try to $operation of empty raw") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmptyRaw) return false

        if (operation != other.operation) return false

        return true
    }

    override fun hashCode(): Int {
        return operation.hashCode()
    }
}