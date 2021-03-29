package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.entity.text.Text

data class Example(
    val text: ForeignText,
    val translation: Text
)