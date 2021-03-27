package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import javax.xml.stream.events.Comment

data class TranslationVariant(
    val meaning: Text,
    val remark: Text?,
    val comment: Text?
) {
    override fun toString(): String = "${remark?.asPlain().orEmpty()} ${meaning.asPlain()} ${comment?.asPlain().orEmpty()}".trim()
}