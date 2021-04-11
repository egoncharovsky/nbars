package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.grammaticalForm
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.FailExpectation

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

        val commentPart = raw.findPart(comment)
        val transcription = raw.findPart(transcription, 0)?.let { textParser.parse(it) as Transcription }
        val grammaticalForm = raw.find(grammaticalForm)?.let { GrammaticalForm.byLabel(it) }

        return when {
            raw.contains(plain) -> {
                val toHeadword = textParser.parse(raw.getPart(plain, 0))
                val comment = commentPart?.let { textParser.parse(it) }

                ReferenceToArticle(transcription, grammaticalForm, toHeadword, examples, comment)
            }
            commentPart?.contains(reference) == true -> {
                val toHeadword = textParser.parse(commentPart)

                ReferenceToArticle(transcription, grammaticalForm, toHeadword, examples)
            }
            else -> throw FailExpectation(
                "Expected reference", raw, reference, "no one",
                "reference both isn't contained in the plain tail of raw (by '$plain') " +
                        "and in comment part (by '$comment'): '$commentPart'"
            )
        }
    }
}