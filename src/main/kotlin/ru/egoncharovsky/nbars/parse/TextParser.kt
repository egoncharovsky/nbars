package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets
import ru.egoncharovsky.nbars.exception.IntersectedRangesFound

class TextParser {

    fun parse(raw: RawPart): Text {
        val langRanges = raw.findMatchesRange(lang).map {
            it to { rawPart: RawPart ->
                val values = rawPart.getGroupValues(lang)
                ForeignText(
                    replaceEscapedBrackets(values[2]),
                    values[1]
                )
            }
        }.toMap()
        val labelRanges = raw.findMatchesRange(label).map {
            it to { rawPart: RawPart -> Abbreviation(rawPart.get(label)) }
        }.toMap()
        val referenceRanges = raw.findMatchesRange(reference).map {
            it to { rawPart: RawPart -> Reference(rawPart.get(reference)) }
        }.toMap()
        val transcriptionRanges = raw.findMatchesRange(transcription).map {
            it to { rawPart: RawPart -> Transcription(rawPart.get(transcription)) }
        }.toMap()

        return parse(raw,
            langRanges
                .plus(labelRanges)
                .plus(referenceRanges)
                .plus(transcriptionRanges)
        )
    }

    private fun parse(raw: RawPart, ranges: Map<IntRange, (RawPart) -> Text>): Text {
        val rangeRegexes = ranges
            .toSortedMap { r1, r2 -> r1.first.compareTo(r2.first) }
        requireNoIntersections(rangeRegexes.keys, raw)

        val plainRanges =
            if (rangeRegexes.isNotEmpty()) {
                rangeRegexes.keys.zipWithNext { a, b -> plainTextRange(a.last + 1, b.first) }
                    .plus(plainTextRange(0, rangeRegexes.firstKey().first))
                    .plus(plainTextRange(rangeRegexes.lastKey().last + 1, raw.length()))
                    .filterNotNull()
            } else {
                listOfNotNull(plainTextRange(0, raw.length()))
            }.map {
                it to { rawPart: RawPart ->
                    PlainText(replaceEscapedBrackets(rawPart.get(plain)))
                }
            }.toMap()

        rangeRegexes.putAll(plainRanges)
        requireNoIntersections(rangeRegexes.keys, raw)

        val parts = raw.cut(*rangeRegexes.keys.toTypedArray())
            .zip(rangeRegexes.keys)
            .map { (rawPart, range) ->
                rangeRegexes[range]!!.invoke(rawPart)
            }

        raw.finishAll()

        return if (parts.size > 1) {
            Sentence(parts)
        } else {
            parts[0]
        }
    }

    private fun requireNoIntersections(ranges: Collection<IntRange>, raw: RawPart) =
        ranges.fold(setOf<Int>()) { acc, range ->
            if (acc.intersect(range).isNotEmpty()) throw IntersectedRangesFound(raw.toString(), ranges.toString())
            acc.plus(range)
        }

    private fun plainTextRange(from: Int, to: Int): IntRange? {
        return if (from < to) from until to else null
    }
}