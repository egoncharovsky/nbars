package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.entity.text.Text.Companion.normalize
import ru.egoncharovsky.nbars.exception.IntersectedRangesFound
import java.util.*

open class TextParser {

    fun parse(raw: RawPart): Text {
        val parts = divide(raw, findLangRanges(raw)) { rawPart ->
            parsePlainParts(rawPart)
        }.flatten()

        return Sentence.textFrom(parts)
    }

    protected fun parsePlainParts(raw: RawPart): List<TextPart> {
        val ranges = listOf(
            ::findLabelRanges,
            ::findReferenceRanges,
            ::findTranscriptionRanges
        ).map {
            it.invoke(raw)
        }.reduce(Map<IntRange, (RawPart) -> TextPart>::plus)

        return divide(raw, ranges) {
            PlainText(normalize(it.get(plain)))
        }
    }

    protected open fun findLangRanges(raw: RawPart) =
        raw.findMatchesRange(lang).map {
            it to { rawPart: RawPart ->
                val values = rawPart.getGroupValues(lang)

                val language = values[1]
                val text = Sentence.textFrom(parsePlainParts(rawPart.attach(values[2])))

                listOf(
                    ForeignText(
                        text,
                        language
                    )
                )
            }
        }.toMap()

    protected open fun findLabelRanges(raw: RawPart) = raw.findMatchesRange(label).map {
        it to { rawPart: RawPart -> Abbreviation(rawPart.get(label)) }
    }.toMap()

    protected open fun findReferenceRanges(raw: RawPart) = raw.findMatchesRange(reference).map {
        it to { rawPart: RawPart -> Reference(rawPart.get(reference)) }
    }.toMap()

    protected open fun findTranscriptionRanges(raw: RawPart) = raw.findMatchesRange(transcription).map {
        it to { rawPart: RawPart -> Transcription(rawPart.get(transcription)) }
    }.toMap()

    private fun findRangesBetween(ranges: List<IntRange>, length: Int): List<IntRange> {
        return if (ranges.isNotEmpty()) {
            ranges.zipWithNext { a, b -> rangeBetween(a.last + 1, b.first) }
                .plus(rangeBetween(0, ranges.first().first))
                .plus(rangeBetween(ranges.last().last + 1, length))
                .filterNotNull()
        } else {
            listOfNotNull(rangeBetween(0, length))
        }
    }

    private fun <P> divide(raw: RawPart, ranges: Map<IntRange, (RawPart) -> P>, default: (RawPart) -> P): List<P> {
        val sortedRanges = ranges
            .toSortedMap { r1, r2 -> r1.first.compareTo(r2.first) }
        requireNoIntersections(sortedRanges.keys, raw)

        val plainRanges = findRangesBetween(sortedRanges.keys.toList(), raw.length())
            .associateWith { default }

        sortedRanges.putAll(plainRanges)
        requireNoIntersections(sortedRanges.keys, raw)

        val parts = raw.cut(*sortedRanges.keys.toTypedArray())
            .zip(sortedRanges.keys)
            .map { (rawPart, range) ->
                sortedRanges[range]!!.invoke(rawPart)
            }

        raw.finishAll()

        return parts
    }

    private fun requireNoIntersections(ranges: Collection<IntRange>, raw: RawPart) =
        ranges.fold(setOf<Int>()) { acc, range ->
            if (acc.intersect(range).isNotEmpty()) throw IntersectedRangesFound(raw.toString(), ranges.toString())
            acc.plus(range)
        }

    private fun rangeBetween(from: Int, to: Int): IntRange? {
        return if (from < to) from until to else null
    }
}