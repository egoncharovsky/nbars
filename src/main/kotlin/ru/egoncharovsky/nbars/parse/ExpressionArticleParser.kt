package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.expressionType
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.text.Transcription

class ExpressionArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val translationParser = TranslationParser()

    fun parse(headword: String, raw: RawPart): ExpressionArticle {
        logger.trace("Parse expression article for '$headword' from: '$raw'")

        raw.removeAll(boldTag)

        val split = raw.split(translationMarker)
        val prefix = split[0]

        logger.trace("Expression prefix: $prefix")

        prefix.removeAll(escapedSquareBrackets)
        val comment = prefix.findPartBefore(comment, translation)?.let {
            textParser.parse(it)
        }
        val transcription = textParser.parse(prefix.getPart(transcription, 0)) as Transcription
        val expressionType = prefix.find(expressionType)?.let { ExpressionType.byLabel(it) }

        val rawTranslations = split.drop(1)

        val translations = if (rawTranslations.isEmpty()) {
            listOf(translationParser.parse(prefix))
        } else {
            rawTranslations.map { translationParser.parse(it) }
        }
        prefix.finishAll()

        return ExpressionArticle(headword, transcription, expressionType, comment, translations)
    }
}