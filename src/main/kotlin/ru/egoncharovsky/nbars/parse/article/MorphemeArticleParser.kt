package ru.egoncharovsky.nbars.parse.article

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.morphemeType
import ru.egoncharovsky.nbars.Regexes.reference
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.article.MorphemeArticle
import ru.egoncharovsky.nbars.entity.article.section.MorphemeArticleSection
import ru.egoncharovsky.nbars.entity.article.section.MorphemeHomonym
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.parse.RawPart
import ru.egoncharovsky.nbars.parse.ReferenceToArticleParser
import ru.egoncharovsky.nbars.parse.TextParser
import ru.egoncharovsky.nbars.parse.TranslationParser

class MorphemeArticleParser : ArticleParser<MorphemeArticleSection>() {

    override val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val referenceToArticleParser = ReferenceToArticleParser()
    private val translationParser = TranslationParser()

    fun parse(headword: String, raw: RawPart): MorphemeArticle {
        logger.trace("Parsing morpheme article $headword from $raw")

        return MorphemeArticle(headword, parseBody(raw))
    }

    override fun parseSection(raw: RawPart): MorphemeArticleSection {
        return when {
            raw.contains(reference) -> referenceToArticleParser.parse(raw)
            else -> parseHomonym(raw)
        }
    }

    private fun parseHomonym(raw: RawPart): MorphemeHomonym {
        logger.trace("Parse homonym from: $raw")

        raw
            .removeAll(boldTag)
            .removeAll(colorTag)

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