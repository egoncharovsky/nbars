package ru.egoncharovsky.nbars.parse.article

import mu.KLogger
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.sectionMarker
import ru.egoncharovsky.nbars.parse.RawPart

abstract class ArticleParser<S> {

    protected abstract val logger: KLogger

    protected fun parseBody(raw: RawPart): List<List<S>> {
        logger.trace("Parse body from: $raw")

        val split = raw.split(homonymMarker).onEach {
            it.removeAll(Regexes.superscriptTag)
        }
        val prefix = split[0]

        logger.trace("Body prefix: $prefix")

        val rawHomonyms = split.drop(1)

        val homonyms = if (rawHomonyms.isEmpty()) {
            listOf(parseSections(prefix))
        } else {
            rawHomonyms.map { parseSections(it) }
        }
        raw.finishAll()

        return homonyms
    }

    private fun parseSections(raw: RawPart): List<S> {
        logger.trace("Parse sections from: $raw")

        val split = raw.split(sectionMarker)
        val prefix = split[0]

        logger.trace("Sections prefix: $prefix")

        val rawLexGramHomonyms = split.drop(1)

        val sections = if (rawLexGramHomonyms.isEmpty()) {
            listOf(parseSection(prefix))
        } else {
            rawLexGramHomonyms.map { parseSection(it) }
        }
        raw.finishAll()

        return sections
    }

    protected abstract fun parseSection(raw: RawPart): S
}