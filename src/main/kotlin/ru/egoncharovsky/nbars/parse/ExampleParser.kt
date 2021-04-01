package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.text.ForeignText

class ExampleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()


    fun parse(raw: RawPart): Example {
        logger.trace("Parse example from: $raw")

        val foreign = textParser.parse(raw.getPart(Regexes.lang, 0)) as ForeignText
        raw.removeBefore(Regexes.dash, Regexes.russianLetter).removeAll(Regexes.commentTag)
        val translation = textParser.parse(raw)

        return Example(foreign, translation)
    }
}