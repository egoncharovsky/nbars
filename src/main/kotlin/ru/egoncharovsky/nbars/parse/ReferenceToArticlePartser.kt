package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription

class ReferenceToArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val exampleParser = ExampleParser()

    fun parse(raw: RawPart): ReferenceToArticle {
        logger.trace("Parse reference to article from: '$raw'")

        val examples = raw.findAllParts(Regexes.example).flatMap { exampleParser.parse(it) }

        raw.removeAll(Regexes.escapedSquareBrackets)

        val transcription = textParser.parse(raw.getPart(Regexes.transcription, 0)) as Transcription
        val grammaticalForm = raw.find(Regexes.grammaticalForm)?.let { GrammaticalForm.byLabel(it) }
        val toHeadword = textParser.parse(raw.getPart(Regexes.plain, 0))

        return ReferenceToArticle(transcription, grammaticalForm, toHeadword, examples)
    }
}