package ru.egoncharovsky.nbars.utils.builder.part

import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.utils.SentenceHelper

interface Idioms <B : Idioms<B>> {

    fun idiom(text: ForeignText, translation: String): B {
        add(Example(text, SentenceHelper.st(translation)))
        return builder()
    }

    fun add(example: Example)

    fun builder(): B
}