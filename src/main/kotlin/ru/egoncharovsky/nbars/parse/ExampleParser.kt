package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.dash
import ru.egoncharovsky.nbars.Regexes.exampleVariantMarker
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.letter
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.sampleVariant
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText

class ExampleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()


    fun parse(raw: RawPart): List<Example> {
        logger.trace("Parse examples from: $raw")

        return if (raw.count(lang) > 1) {
            val split = raw.split(exampleVariantMarker)
            val prefix = split[0]

            val foreign = textParser.parse(prefix.getPart(lang, 0)).also {
                prefix.removeAll(dash)
            } as ForeignText

            split.drop(1).flatMap { rawSamples ->
                val translation = textParser.parse(rawSamples.getPartBefore(plain, lang))
                val samples = rawSamples.findAllParts(sampleVariant, 0).map { parseExample(it) }
                listOf(Example(foreign, translation)).plus(samples)
            } // shall we need add special group/abstraction for sample?
        } else {
            listOf(parseExample(raw))
        }
    }

    private fun parseExample(raw: RawPart): Example {
        logger.trace("Parse example from: $raw")

        val foreign = textParser.parse(raw.getPart(lang, 0)) as ForeignText
        raw.removeBefore(dash, letter).removeAll(commentTag)
        val translation = textParser.parse(raw)

        return Example(foreign, translation)
    }
}