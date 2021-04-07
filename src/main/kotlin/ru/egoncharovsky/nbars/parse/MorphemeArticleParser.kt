package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Either
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.homonymMarker
import ru.egoncharovsky.nbars.Regexes.meaningVariantMarker
import ru.egoncharovsky.nbars.Regexes.morphemeType
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.superscriptTag
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translationTag
import ru.egoncharovsky.nbars.entity.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.section.ReferenceToArticle
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.entity.translation.Variant

class MorphemeArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val referenceToArticleParser = ReferenceToArticleParser()
    private val exampleParser = ExampleParser()

    fun parse(headword: String, raw: RawPart): MorphemeArticle {
        logger.trace("Parsing morpheme article $headword from $raw")

        raw
            .removeAll(colorTag)
            .removeAll(commentTag)
            .removeAll(translationTag)

        val homonyms = parseBody(raw)
        raw.finishAll()

        return MorphemeArticle(headword, homonyms)
    }

    private fun parseBody(raw: RawPart): Either<List<MorphemeHomonym>, ReferenceToArticle> {
        return when {
            raw.contains(reference) -> Either.Right(referenceToArticleParser.parse(raw))
            else -> Either.Left(parseMeanings(raw))
        }
    }

    private fun parseMeanings(raw: RawPart): List<MorphemeHomonym> {
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

        val split = raw.split(meaningVariantMarker)
        val prefix = split[0]

        logger.trace("Meaning prefix: $prefix")

        prefix.removeAll(escapedSquareBrackets)
        val transcription = textParser.parse(prefix.getPart(transcription, 0)) as Transcription
        val morphemeType = prefix.find(morphemeType)?.let { MorphemeType.byLabel(it) }

        val rawVariants = split.drop(1)

        val variants = if (rawVariants.isEmpty()) {
            listOf(parseVariant(prefix))
        } else {
            rawVariants.map { parseVariant(it) }
        }
        val comment = prefix.findPart(plain)?.let { textParser.parse(it) }
        raw.finishAll()

        return MorphemeHomonym(transcription, morphemeType, Translation(variants, comment))
    }

    private fun parseVariant(raw: RawPart): Variant {
        logger.trace("Parse variant from: $raw")

        val examples = raw.findAllParts(example).flatMap { exampleParser.parse(it) }
        val meaning = textParser.parse(raw.getPart(plain))
        raw.finishAll()

        return Variant(meaning, examples)
    }

}