package ru.egoncharovsky.nbars

import ru.egoncharovsky.nbars.entity.*


object Regexes {

    val headwordMarker = "^[^\\t#\uFEFF]".toRegex()
    val dictionaryEndMarker = "\\{\\{ The End }}".toRegex()

    val marginTag = "\\[/?m\\d?]".toRegex()
    val boldTag = "\\[/?b]".toRegex()
    val italicTag = "\\[/?i]".toRegex()
    val colorTag = "\\[/?c( .*?)?]".toRegex()
    val optionalTag = "\\[/?\\*]".toRegex()
    val commentTag = "\\[/?com]".toRegex()

    val braces = "[{}]".toRegex()
    val doubleBraces = "\\{\\{|}}".toRegex()
    val plain = "(.+)".toRegex()
    val dash = "—".toRegex()
    val squareBrackets = "[\\[\\]]".toRegex()

    val escapedSquareBrackets = "(\\\\\\[|\\\\])".toRegex()
    val leftEscapedSquareBracket = "\\\\\\[".toRegex()
    val rightEscapedSquareBracket = "\\\\]".toRegex()

    val russianLetter = "[а-яА-Я]".toRegex()

    val homonymMarker = "\\[sup]\\d+?\\[/sup]".toRegex()
    val lexicalGrammarHomonymMarker = "[Ⅰ-Ⅹ]".toRegex()
    val translationMarker = "\\d+?\\.".toRegex()
    val translationVariantMarker = "\\d+?\\)".toRegex()

    val transcription = "\\[t](.+?)\\[/t]".toRegex()
    val translation = "\\[trn](.+?)\\[/trn]".toRegex()
    val reference = "<<(.+?)>>".toRegex()
    val label = "\\[p](.+?)\\[/p]".toRegex()
    val comment = "\\[com](.+?)\\[/com]".toRegex()
    val lang = "\\[lang id=(\\d+)](.+?)\\[/lang]".toRegex()
    val example = "\\[ex](.+?)\\[/ex]".toRegex()
    val stress = "\\['](.)\\[/']".toRegex()

    val partOfSpeech = "\\[p](${PartOfSpeech.labels.joinToString("|")})\\[/p]".toRegex()
    val expressionType = "\\[p](${ExpressionType.labels.joinToString("|")})\\[/p]".toRegex()
    val referenceType = "\\[p](${ReferenceType.labels.joinToString("|")})\\[/p]".toRegex()
}
