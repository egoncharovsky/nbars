package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.exception.UnknownLabel

enum class ReferenceType(val label: String) {
    PLURAL("pl");

    companion object {
        val labels by lazy {
            listOf(PLURAL.label)
        }

        fun byLabel(label: String): ReferenceType {
            return values().find { it.label == label }
                ?: throw UnknownLabel(ReferenceType::class.simpleName!!, label)
        }
    }
}