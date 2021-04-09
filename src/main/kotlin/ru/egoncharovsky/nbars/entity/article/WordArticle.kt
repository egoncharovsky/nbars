package ru.egoncharovsky.nbars.entity.article

import ru.egoncharovsky.nbars.entity.article.section.WordArticleSection
import ru.egoncharovsky.nbars.entity.text.Text

data class WordArticle(
    override val headword: String,
    override val homonyms: List<List<WordArticleSection>>,
    val compound: Text? = null
) : Article<WordArticleSection>