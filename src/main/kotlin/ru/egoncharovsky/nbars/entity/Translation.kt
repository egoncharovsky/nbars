package ru.egoncharovsky.nbars.entity

import ru.egoncharovsky.nbars.entity.text.Text
import javax.xml.stream.events.Comment

data class Translation(
    val variants: List<TranslationVariant>,
    val remark: Text?,
    val comment: Text?
) {
    override fun toString(): String = "${remark?.asPlain().orEmpty()} ${comment?.asPlain().orEmpty()}".trim()
}