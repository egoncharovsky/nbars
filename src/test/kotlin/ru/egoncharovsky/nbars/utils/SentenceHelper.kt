package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.parse.RawPart
import ru.egoncharovsky.nbars.parse.TextParser

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
                is TextPart -> it
                else -> throw IllegalArgumentException("Incorrect type for $it")
            }
        }
        return Sentence(texts)
    }

    fun st(raw: String): Text {
        val rawPart = RawPart(raw)

        val ranges = listOf(
            SentenceHelper::findLangRanges,
            SentenceHelper::findLabelRanges,
            SentenceHelper::findReferenceRanges,
            SentenceHelper::findTranscriptionRanges
        ).map {
            it.invoke(rawPart)
        }.reduce(Map<IntRange, (RawPart) -> TextPart>::plus)

        return textParser.parse(rawPart, ranges)
    }

    fun stn(raw: String?): Text? = raw?.let { st(it) }

    fun pt(s: String) = PlainText(Text.normalize(s))
    fun ft(s: String, lang: String) = ForeignText(Text.normalize(s), lang)
    fun ab(s: String) = Abbreviation(s)
    fun rf(s: String) = Reference(s)
    fun tr(s: String) = Transcription(s)

    fun eng(s: String) = ForeignText(Text.normalize(s), "1033")
}