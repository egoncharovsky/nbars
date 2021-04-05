package ru.egoncharovsky.nbars.entity.translation

import ru.egoncharovsky.nbars.entity.text.Text

interface Translation {
    val remark: Text?
    val comment: Text?
    val variants: List<Variant>
}