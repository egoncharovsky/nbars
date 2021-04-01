package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.commentTag
import ru.egoncharovsky.nbars.Regexes.dash
import ru.egoncharovsky.nbars.Regexes.example
import ru.egoncharovsky.nbars.Regexes.label
import ru.egoncharovsky.nbars.Regexes.lang
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationVariantMarker
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.entity.Translation
import ru.egoncharovsky.nbars.entity.text.ForeignText
import ru.egoncharovsky.nbars.exception.ParseException

class TranslationParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

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
                remark = prefix.findPart(label, 0)?.let { textParser.parse(it) }
            )
        }.also {
            prefix.finishAll()
        }
    }

    internal fun parseVariant(raw: RawPart): Translation.Variant {
        logger.trace("Parse translation variant from: $raw")

        val examples = raw.findAllParts(example).map { parseExample(it) }

        return when {
            raw.contains(translation) -> {
                val meaning = textParser.parse(raw.getPart(translation).removeAll(commentTag))

                val comment = raw.findPart(comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(plain, 0)?.let { textParser.parse(it) }

                Translation.Variant(meaning, remark, comment, examples)
            }
            raw.contains(reference) -> {
                val comment = raw.findPart(comment)?.let { textParser.parse(it) }
                val remark = raw.findPart(label, 0)?.let { textParser.parse(it) }

                val reference = textParser.parse(raw)

                Translation.Variant(reference, remark, comment, examples)
            }
            else -> throw ParseException(
                "meaning",
                "no translation by '$translation' or reference by '${reference}' found",
                raw
            )
        }.also {
            raw.finishAll()
        }
    }

    private fun parseExample(raw: RawPart): Example {

        val foreign = textParser.parse(raw.getPart(lang, 0)) as ForeignText
        val comment = raw.findPart(comment)?.let { textParser.parse(it) }
        val translation = textParser.parse(raw.remove(dash))

        return Example(foreign, translation, comment)
    }
}