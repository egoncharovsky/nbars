package ru.egoncharovsky.nbars.utils.builder.part

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.utils.SentenceHelper
import ru.egoncharovsky.nbars.utils.SentenceHelper.st

interface Examples<B : Examples<B>> {

    fun example(text: ForeignText, translation: String): B {
        add(Example(text, st(translation)))
        return builder()
    }

    fun add(example: Example)

    fun builder(): B
}