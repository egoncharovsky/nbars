package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.grammaticalForm
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription

class ReferenceToArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val exampleParser = ExampleParser()

    fun parse(raw: RawPart): ReferenceToArticle {
        logger.trace("Parse reference to article from: '$raw'")

        raw
            .removeAll(Regexes.boldTag)
            .removeAll(colorTag)

        val examples = raw.findAllParts(example).flatMap { exampleParser.parse(it) }

        raw.removeAll(escapedSquareBrackets)

        val transcription = textParser.parse(raw.getPart(transcription, 0)) as Transcription
        val grammaticalForm = raw.find(grammaticalForm)?.let { GrammaticalForm.byLabel(it) }
        val toHeadword = textParser.parse(raw.getPart(plain, 0))

        return ReferenceToArticle(transcription, grammaticalForm, toHeadword, examples)
    }
}