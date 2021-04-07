package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.morphemeType
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.superscriptTag
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription

class MorphemeArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val referenceToArticleParser = ReferenceToArticleParser()
    private val translationParser = TranslationParser()

    fun parse(headword: String, raw: RawPart): MorphemeArticle {
        logger.trace("Parsing morpheme article $headword from $raw")

        raw.removeAll(colorTag)

        val homonyms = parseBody(raw)
        raw.finishAll()

        return MorphemeArticle(headword, homonyms)
    }

    private fun parseBody(raw: RawPart): Either<List<MorphemeHomonym>, ReferenceToArticle> {
        return when {
            raw.contains(reference) -> Either.Right(referenceToArticleParser.parse(raw))
            else -> Either.Left(parseHomonyms(raw))
        }
    }

    private fun parseHomonyms(raw: RawPart): List<MorphemeHomonym> {
        logger.trace("Parse meanings from: $raw")

        val split = raw.split(homonymMarker).onEach {
            it.removeAll(boldTag).removeAll(superscriptTag)
        }
        val prefix = split[0]

        logger.trace("Body prefix: $prefix")

        val rawMeanings = split.drop(1)

        val meanings = if (rawMeanings.isEmpty()) {
            listOf(parseHomonym(prefix))
        } else {
            rawMeanings.map { parseHomonym(it) }
        }
        prefix.finishAll()

        return meanings
    }

    private fun parseHomonym(raw: RawPart): MorphemeHomonym {
        logger.trace("Parse homonym from: $raw")

        val split = raw.split(translationMarker)
        val prefix = split[0]

        logger.trace("Translation prefix: $prefix")

        prefix.removeAll(escapedSquareBrackets)
        val transcription = textParser.parse(prefix.getPart(transcription, 0)) as Transcription
        val morphemeType = prefix.find(morphemeType)?.let { MorphemeType.byLabel(it) }

        val rawVariants = split.drop(1)

        return if (rawVariants.isEmpty()) {
            MorphemeHomonym(
                transcription,
                morphemeType,
                listOf(translationParser.parse(prefix))
            )
        } else {
            MorphemeHomonym(
                transcription,
                morphemeType,
                rawVariants.map { translationParser.parse(it) },
                comment = prefix.findPart(comment)?.let { textParser.parse(it) }
            )

        }.also {
            raw.finishAll()
        }
    }
}