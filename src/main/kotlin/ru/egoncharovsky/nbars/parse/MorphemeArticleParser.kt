package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.expressionType
import ru.egoncharovsky.nbars.Regexes.meaningVariantMarker
import ru.egoncharovsky.nbars.Regexes.morphemeType
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.Regexes.translationTag
import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.MorphemeMeaning
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.article.*
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.exception.StepParseException

class MorphemeArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val referenceToArticleParser = ReferenceToArticleParser()
    private val exampleParser = ExampleParser()

    fun parse(headword: String, raw: RawPart): MorphemeArticle {
        logger.trace("Parsing morpheme article $headword from $raw")

        raw.removeAll(commentTag).removeAll(translationTag)

        val homonyms = parseBody(raw)
        raw.finishAll()

        return MorphemeArticle(headword, homonyms)
    }

    private fun parseBody(raw: RawPart): Either<List<MorphemeMeaning>, ReferenceToArticle> {
        return when {
            raw.contains(Regexes.reference) -> Either.Right(referenceToArticleParser.parse(raw))
            else -> Either.Left(parseMeanings(raw))
        }
    }

    private fun parseMeanings(raw: RawPart): List<MorphemeMeaning> {
        logger.trace("Parse meanings from: $raw")

        val split = raw.split(Regexes.homonymMarker).onEach {
            it.removeAll(Regexes.boldTag).removeAll(Regexes.superscriptTag)
        }
        val prefix = split[0]

        logger.trace("Body prefix: $prefix")

        val rawMeanings = split.drop(1)

        val meanings = if (rawMeanings.isEmpty()) {
            listOf(parseMeaning(prefix))
        } else {
            rawMeanings.map { parseMeaning(it) }
        }
        prefix.finishAll()

        return meanings
    }

    private fun parseMeaning(raw: RawPart): MorphemeMeaning {
        logger.trace("Parse meaning from: $raw")

        val split = raw.split(meaningVariantMarker)
        val prefix = split[0]

        logger.trace("Meaning prefix: $prefix")

        prefix.removeAll(Regexes.escapedSquareBrackets)
        val transcription = textParser.parse(prefix.getPart(Regexes.transcription, 0)) as Transcription
        val morphemeType = prefix.find(morphemeType)?.let { MorphemeType.byLabel(it) }

        val rawVariants = split.drop(1)

        val variants =  if (rawVariants.isEmpty()) {
            listOf(parseVariant(prefix))
        } else {
            rawVariants.map { parseVariant(it) }
        }
        val comment = prefix.findPart(plain)?.let { textParser.parse(it) }
        raw.finishAll()

        return MorphemeMeaning(transcription, variants, morphemeType, comment)
    }

    private fun parseVariant(raw: RawPart): MorphemeMeaning.Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(Regexes.example).map { exampleParser.parse(it) }
        val meaning = textParser.parse(raw.getPart(plain))
        raw.finishAll()

        return MorphemeMeaning.Variant(meaning, examples)
    }

}