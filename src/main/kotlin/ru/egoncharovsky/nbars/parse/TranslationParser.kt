package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.referencePart
import ru.egoncharovsky.nbars.Regexes.superscriptTag
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationVariantMarker
import ru.egoncharovsky.nbars.entity.text.Sentence.Companion.join
import ru.egoncharovsky.nbars.entity.translation.Translation
import ru.egoncharovsky.nbars.entity.translation.Variant
import ru.egoncharovsky.nbars.exception.StepParseException

class TranslationParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val exampleParser = ExampleParser()

    fun parse(raw: RawPart): Translation {
        logger.trace("Parse translation from: $raw")

        val split = raw.split(translationVariantMarker)
        val prefix: RawPart = split[0]

        logger.trace("Translation prefix: $prefix")

        val rawVariants = split.drop(1)

        return if (rawVariants.isEmpty()) {
            Translation(listOf(parseVariant(prefix)))
        } else {
            Translation(
                rawVariants.map { parseVariant(it) },
                comment = prefix.findPart(comment)?.let { textParser.parse(it) },
                remark = prefix.findPart(plain)?.let { textParser.parse(it) }
            )
        }.also {
            prefix.finishAll()
        }
    }

    internal fun parseVariant(raw: RawPart): Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(example).flatMap { exampleParser.parse(it) }

        return when {
            raw.contains(translation) -> {
                val meaning = textParser.parse(raw.getPart(translation).removeAll(commentTag))

                val comment = raw.findAllParts(comment).map { textParser.parse(it) }.let { join(it, " ") }
                val remark = raw.findPart(plain)?.let { textParser.parse(it) }

                Variant(meaning, examples, remark, comment)
            }
            raw.contains(reference) -> {
                val reference = textParser.parse(raw.getPart(referencePart, 0).removeAll(superscriptTag))

                val comment = raw.findPart(comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(plain)?.let { textParser.parse(it) }

                Variant(reference, examples, remark, comment)
            }
            else -> throw StepParseException(
                "variant",
                "no translation by '$translation' or reference by '${reference}' found",
                raw
            )
        }.also {
            raw.finishAll()
        }
    }

}