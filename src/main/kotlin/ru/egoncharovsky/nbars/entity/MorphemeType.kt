package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.exception.UnknownLabel

enum class MorphemeType(val label: String) {
    SUFFIX("suff");

    companion object {
        val labels by lazy {
            listOf(SUFFIX.label)
        }

        fun byLabel(label: String): MorphemeType {
            return values().find { it.label == label }
                ?: throw UnknownLabel(MorphemeType::class.simpleName!!, label)
        }
    }
}