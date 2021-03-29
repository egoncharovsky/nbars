package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.leftEscapedSquareBracket
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.rightEscapedSquareBracket
import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets

class TextParser {

    fun parse(raw: RawPart): Text {
        val langRanges = raw.findMatchesRange(lang).map { it to ForeignText::class }.toMap()
        val labelRanges = raw.findMatchesRange(label).map { it to Abbreviation::class }.toMap()

        val rangeRegexes = langRanges.plus(labelRanges)
            .toSortedMap { r1, r2 -> r1.first.compareTo(r2.first) }
        requireNoIntersections(rangeRegexes.keys) { "Intersected tags found in $raw" }

        val plainRanges =
            if (rangeRegexes.isNotEmpty()) {
                rangeRegexes.keys.zipWithNext { a, b -> plainTextRange(a.last + 1, b.first) }
                    .plus(plainTextRange(0, rangeRegexes.firstKey().first))
                    .plus(plainTextRange(rangeRegexes.lastKey().last + 1, raw.length()))
                    .filterNotNull()
            } else {
                listOfNotNull(plainTextRange(0, raw.length()))
            }.map { it to PlainText::class }.toMap()

        rangeRegexes.putAll(plainRanges)
        requireNoIntersections(rangeRegexes.keys) { "Intersected tags found in $raw" }

        val parts = raw.cut(*rangeRegexes.keys.toTypedArray()).zip(rangeRegexes.keys).map { (rawPart, range) ->
            when (val type = rangeRegexes[range]!!) {
                ForeignText::class -> {
                    val values = rawPart.getGroupValues(lang)
                    ForeignText(
                        replaceEscapedBrackets(values[2]),
                        values[1]
                    )
                }
                Abbreviation::class -> Abbreviation(rawPart.get(label))
                PlainText::class -> PlainText(replaceEscapedBrackets(rawPart.get(plain)))
                else -> throw IllegalStateException("Unknown type: $type")
            }
        }
        raw.finishAll()

        return if (parts.size > 1) {
            Sentence(parts)
        } else {
            parts[0]
        }
    }

    private fun requireNoIntersections(ranges: Collection<IntRange>, lazyMessage: () -> Any) =
        ranges.fold(setOf<Int>()) { acc, range ->
            require(acc.intersect(range).isEmpty(), lazyMessage)
            acc.plus(range)
        }

    private fun plainTextRange(from: Int, to: Int): IntRange? {
        return if (from < to) from until to else null
    }
}