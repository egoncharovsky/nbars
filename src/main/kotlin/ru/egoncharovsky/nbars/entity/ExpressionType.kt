package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.exception.UnknownLabel

enum class ExpressionType(val label: String) {
    PHRASAL_VERB("phr v");

    companion object {
        val labels by lazy {
            listOf(PHRASAL_VERB.label)
        }

        fun byLabel(label: String): ExpressionType {
            return values().find { it.label == label }
                ?: throw UnknownLabel(ExpressionType::class.simpleName!!, label)
        }
    }
}