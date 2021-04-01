package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.Regexes.escapedSquareBrackets
import ru.egoncharovsky.nbars.Regexes.plain
import ru.egoncharovsky.nbars.Regexes.referenceType
import ru.egoncharovsky.nbars.Regexes.transcription
import ru.egoncharovsky.nbars.entity.ReferenceType
import ru.egoncharovsky.nbars.entity.article.ReferenceArticle
import ru.egoncharovsky.nbars.entity.text.Transcription

class ReferenceArticleParser {

    private val logger = KotlinLogging.logger { }
    private val textParser = TextParser()

    fun parse(headword: String, raw: RawPart): ReferenceArticle {
        logger.trace("Parse reference article for '$headword' from: '$raw'")

        raw.removeAll(escapedSquareBrackets)

        val transcription = textParser.parse(raw.getPart(transcription, 0)) as Transcription
        val referenceType = raw.find(referenceType)?.let { ReferenceType.byLabel(it) }
        val toHeadword = textParser.parse(raw.getPart(plain, 0))

        return ReferenceArticle(headword, transcription, referenceType, toHeadword)
    }
}