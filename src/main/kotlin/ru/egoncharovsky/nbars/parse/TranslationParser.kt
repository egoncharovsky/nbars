package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationVariantMarker
import ru.egoncharovsky.nbars.entity.Translation
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

    internal fun parseVariant(raw: RawPart): Translation.Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(example).map { exampleParser.parse(it) }

        return when {
            raw.contains(translation) -> {
                val meaning = textParser.parse(raw.getPart(translation).removeAll(commentTag))

                val comment = raw.findPart(comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(plain)?.let { textParser.parse(it) }

                Translation.Variant(meaning, remark, comment, examples)
            }
            raw.contains(reference) -> {
                val comment = raw.findPart(comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(label, 0)?.let { textParser.parse(it) }

                val reference = textParser.parse(raw)

                Translation.Variant(reference, remark, comment, examples)
            }
            else -> throw StepParseException(
                "meaning",
                "no translation by '$translation' or reference by '${reference}' found",
                raw
            )
        }.also {
            raw.finishAll()
        }
    }

}