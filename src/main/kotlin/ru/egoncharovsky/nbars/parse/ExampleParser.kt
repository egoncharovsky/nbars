package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.dash
import ru.egoncharovsky.nbars.Regexes.exampleVariantMarker
import ru.egoncharovsky.nbars.Regexes.idiomPrefix
import ru.egoncharovsky.nbars.Regexes.labelTag
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.letter
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.sampleVariant
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.exception.FailExpectation

class ExampleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

    fun parseIdioms(raw: RawPart): List<Example> {
        raw.removeBefore(Regexes.idiomMarker, lang)

        val idioms = raw.getAllParts(Regexes.example).flatMap { parse(it) }
        raw.finishAll()

        return idioms
    }

    fun parse(raw: RawPart): List<Example> {
        logger.trace("Parse examples from: $raw")

        raw.removeAll(commentTag)

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
        }.also {
            raw.finishAll()
        }
    }

    private fun parseExample(raw: RawPart): Example {
        logger.trace("Parse example from: $raw")

        //todo add support of abbreviations
        val foreign = textParser.parse(raw.getPart(lang, 0).removeAll(labelTag)) as ForeignText
        raw.removeBefore(dash, letter)

        if (raw.contains(idiomPrefix))
            throw FailExpectation(
                "No idiom marker expected", raw, idiomPrefix, "some",
                "translation of example should not starts with idiom prefix"
            )
        val translation = textParser.parse(raw)

        return Example(foreign, translation)
    }
}