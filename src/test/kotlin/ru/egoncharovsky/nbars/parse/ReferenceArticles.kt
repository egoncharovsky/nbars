package ru.egoncharovsky.nbars.parse

import ru.egoncharovsky.nbars.entity.ReferenceType
import ru.egoncharovsky.nbars.entity.article.ReferenceArticle
import ru.egoncharovsky.nbars.utils.SentenceHelper.st
import ru.egoncharovsky.nbars.utils.SentenceHelper.tr

object ReferenceArticles {
    val peet = ReferenceArticle("peet", tr("piːt"), toHeadWord = st("= rf(pete)"))
    val ible = ReferenceArticle("-ible", tr("-əb(ə)l"), toHeadWord = st("= rf(-able) 2, 3"))
    val pence = ReferenceArticle("pence", tr("pens"), ReferenceType.PLURAL, toHeadWord = st("ab(от) rf(penny)"))
}