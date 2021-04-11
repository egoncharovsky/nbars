package ru.egoncharovsky.nbars.utils

import ru.egoncharovsky.nbars.entity.text.*
import ru.egoncharovsky.nbars.parse.RawPart
import ru.egoncharovsky.nbars.parse.TextParser

object SentenceHelper : TextParser() {
    private val ft = "ft\\((.+?),(\\d+?)\\)".toRegex()
    private val ab = "ab\\((.+?)\\)".toRegex()
    private val rf = "rf\\((.+?)\\)".toRegex()
    private val tr = "tr\\((.+?)\\)".toRegex()

    private val textParser = TextParser()

    override fun findLangRanges(raw: RawPart) =
        raw.findMatchesRange(ft).map {
            it to { rawPart: RawPart ->
                val values = rawPart.getGroupValues(ft)

                val text = Sentence.textFrom(parsePlainParts(rawPart.attach(values[1])))
                val language = values[2]

                listOf(
                    ForeignText(
                        text,
                        language
                    )
                )
            }
        }.toMap()

    override fun findLabelRanges(raw: RawPart) = raw.findMatchesRange(ab).associateWith {
        { rawPart: RawPart -> Abbreviation(rawPart.get(ab)) }
    }

    override fun findReferenceRanges(raw: RawPart) = raw.findMatchesRange(rf).associateWith {
        { rawPart: RawPart -> Reference(rawPart.get(rf)) }
    }

    override fun findTranscriptionRanges(raw: RawPart) = raw.findMatchesRange(tr).associateWith {
        { rawPart: RawPart -> Transcription(rawPart.get(tr)) }
    }

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

    fun st(raw: String): Text = parse(RawPart(raw))
    fun stn(raw: String?): Text? = raw?.let { st(it) }

    fun pt(s: String) = PlainText(Text.normalize(s))

    fun ft(s: String, lang: String) = ForeignText(st(s), lang)

    fun ab(s: String) = Abbreviation(s)

    fun rf(s: String) = Reference(s)

    fun tr(s: String) = Transcription(s)
    fun trn(s: String?) = s?.let { tr(it) }

    fun eng(s: String) = ForeignText(st(s), "1033")
}