package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.entity.text.*

object SentenceHelper {
    private val ft = "ft\\((.+?),(.+?)\\)".toRegex()
    private val ab = "ab\\((.+?)\\)".toRegex()
    private val rf = "rf\\((.+?)\\)".toRegex()
    private val tr = "tr\\((.+?)\\)".toRegex()

    private val textParser = TextParser()

    private fun findLangRanges(raw: RawPart) =
        raw.findMatchesRange(ft).map {
            it to { rawPart: RawPart ->
                val values = rawPart.getGroupValues(ft)
                ft(values[1], values[2])
            }
        }.toMap()

    private fun findLabelRanges(raw: RawPart) = raw.findMatchesRange(ab).map {
        it to { rawPart: RawPart -> Abbreviation(rawPart.get(ab)) }
    }.toMap()

    private fun findReferenceRanges(raw: RawPart) = raw.findMatchesRange(rf).map {
        it to { rawPart: RawPart -> Reference(rawPart.get(rf)) }
    }.toMap()

    private fun findTranscriptionRanges(raw: RawPart) = raw.findMatchesRange(tr).map {
        it to { rawPart: RawPart -> Transcription(rawPart.get(tr)) }
    }.toMap()

    fun st(vararg parts: Any): Sentence {
        val texts = parts.map {
            when (it) {
                is String -> pt(it)
                is Text -> it
                else -> throw IllegalArgumentException("Incorrect type for $it")
            }
        }
        return Sentence(texts)
    }

    fun st(raw: String): Text {
        val rawPart = RawPart(raw)

        val ranges = listOf(
            ::findLangRanges,
            ::findLabelRanges,
            ::findReferenceRanges,
            ::findTranscriptionRanges
        ).map {
            it.invoke(rawPart)
        }.reduce(Map<IntRange, (RawPart) -> Text>::plus)

        return textParser.parse(rawPart, ranges)
    }

    fun pt(s: String) = PlainText(Text.replaceEscapedBrackets(s))
    fun ft(s: String, lang: String) = ForeignText(Text.replaceEscapedBrackets(s), lang)
    fun ab(s: String) = Abbreviation(s)
    fun rf(s: String) = Reference(s)
    fun tr(s: String) = Transcription(s)
}