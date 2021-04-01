package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.entity.text.Text.Companion.replaceEscapedBrackets
import ru.egoncharovsky.nbars.exception.IntersectedRangesFound
import java.util.*
import kotlin.jvm.internal.Reflection

class TextParser {

    fun parse(raw: RawPart): Text {
        val ranges = listOf(
            ::findLangRanges,
            ::findLabelRanges,
            ::findReferenceRanges,
            ::findTranscriptionRanges
        ).map {
            it.invoke(raw)
        }.reduce(Map<IntRange, (RawPart) -> Text>::plus)

        return parse(raw, ranges)
    }

    private fun findLangRanges(raw: RawPart) =
        raw.findMatchesRange(lang).map {
            it to { rawPart: RawPart ->
                val values = rawPart.getGroupValues(lang)
                ForeignText(
                    replaceEscapedBrackets(values[2]),
                    values[1]
                )
            }
        }.toMap()

    private fun findLabelRanges(raw: RawPart) = raw.findMatchesRange(label).map {
        it to { rawPart: RawPart -> Abbreviation(rawPart.get(label)) }
    }.toMap()

    private fun findReferenceRanges(raw: RawPart) = raw.findMatchesRange(reference).map {
        it to { rawPart: RawPart -> Reference(rawPart.get(reference)) }
    }.toMap()

    private fun findTranscriptionRanges(raw: RawPart) = raw.findMatchesRange(transcription).map {
        it to { rawPart: RawPart -> Transcription(rawPart.get(transcription)) }
    }.toMap()

    private fun findPlainTextRanges(
        ranges: SortedMap<IntRange, (RawPart) -> Text>,
        raw: RawPart
    ): Map<IntRange, (RawPart) -> PlainText> {
        val plainRanges = if (ranges.isNotEmpty()) {
            ranges.keys.zipWithNext { a, b -> plainTextRange(a.last + 1, b.first) }
                .plus(plainTextRange(0, ranges.firstKey().first))
                .plus(plainTextRange(ranges.lastKey().last + 1, raw.length()))
                .filterNotNull()
        } else {
            listOfNotNull(plainTextRange(0, raw.length()))
        }

        return plainRanges.map {
            it to { rawPart: RawPart ->
                PlainText(replaceEscapedBrackets(rawPart.get(plain)))
            }
        }.toMap()
    }

    private fun parse(raw: RawPart, ranges: Map<IntRange, (RawPart) -> Text>): Text {
        val sortedRanges = ranges
            .toSortedMap { r1, r2 -> r1.first.compareTo(r2.first) }
        requireNoIntersections(sortedRanges.keys, raw)

        val plainRanges = findPlainTextRanges(sortedRanges, raw)

        sortedRanges.putAll(plainRanges)
        requireNoIntersections(sortedRanges.keys, raw)

        val parts = raw.cut(*sortedRanges.keys.toTypedArray())
            .zip(sortedRanges.keys)
            .map { (rawPart, range) ->
                sortedRanges[range]!!.invoke(rawPart)
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