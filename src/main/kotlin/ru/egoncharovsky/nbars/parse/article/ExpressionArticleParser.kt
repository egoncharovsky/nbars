package ru.egoncharovsky.nbars.parse.article

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes
import ru.egoncharovsky.nbars.Regexes.boldTag
import ru.egoncharovsky.nbars.Regexes.colorTag
import ru.egoncharovsky.nbars.Regexes.comment
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.expressionType
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.Regexes.translation
import ru.egoncharovsky.nbars.Regexes.translationMarker
import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.article.ExpressionArticle
import ru.egoncharovsky.nbars.entity.article.section.ExpressionArticleSection
import ru.egoncharovsky.nbars.entity.article.section.ExpressionHomonym
import ru.egoncharovsky.nbars.entity.text.Transcription
import ru.egoncharovsky.nbars.parse.*

class ExpressionArticleParser : ArticleParser<ExpressionArticleSection>() {

    override val logger = KotlinLogging.logger { }
    private val textParser = TextParser()
    private val translationParser = TranslationParser()
    private val referenceToArticleParser = ReferenceToArticleParser()
    private val exampleParser = ExampleParser()

    fun parse(headword: String, raw: RawPart): ExpressionArticle {
        logger.trace("Parse expression article for '$headword' from: '$raw'")

        return ExpressionArticle(headword, parseBody(raw))
    }

    override fun parseSection(raw: RawPart): ExpressionArticleSection {
        return when {
            !raw.contains(translation) && raw.contains(Regexes.reference) -> referenceToArticleParser.parse(raw)
            else -> parseHomonym(raw)
        }
    }

    private fun parseHomonym(raw: RawPart): ExpressionHomonym {
        logger.trace("Parse expression homonym: $raw")

        raw
            .removeAll(boldTag)
            .removeAll(colorTag)

        val idioms = raw.findPart(Regexes.idioms, 0)?.let { exampleParser.parseIdioms(it) }

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
        val remark = prefix.findPart(plain)?.let { textParser.parse(it) }
        prefix.finishAll()

        return ExpressionHomonym(transcription, expressionType, translations, remark, comment, idioms)
    }
}