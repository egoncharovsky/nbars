package ru.egoncharovsky.nbars

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.PartOfSpeech


object Regexes {

    val headwordMarker = "^[^\\t#\uFEFF]".toRegex()
    val dictionaryEndMarker = "\\{\\{ The End }}".toRegex()

    val marginTag = "\\[/?m\\d?]".toRegex()
    val boldTag = "\\[/?b]".toRegex()
    val italicTag = "\\[/?i]".toRegex()
    val colorTag = "\\[/?c( .*?)?]".toRegex()
    val optionalTag = "\\[/?\\*]".toRegex()
    val commentTag = "\\[/?com]".toRegex()
    val superscriptTag = "\\[/?sup]".toRegex()
    val subscriptTag = "\\[/?sub]".toRegex()
    val translationTag = "\\[/?trn]".toRegex()

    val braces = "[{}]".toRegex()
    val doubleBraces = "\\{\\{|}}".toRegex()
    val plain = "(.+)".toRegex()
    val dash = "—".toRegex()
    val squareBrackets = "[\\[\\]]".toRegex()

    val escapedSquareBrackets = "(\\\\\\[|\\\\])".toRegex()
    val leftEscapedSquareBracket = "\\\\\\[".toRegex()
    val rightEscapedSquareBracket = "\\\\]".toRegex()

    val letter = "[a-zA-Zа-яА-Я]".toRegex()

    val homonymMarker = "\\[b]\\[sup]\\d+?\\[/sup]\\[/b]".toRegex()
    val lexicalGrammarHomonymMarker = "[Ⅰ-Ⅹ]".toRegex()
    val translationMarker = "\\d+?\\. ".toRegex()
    val translationVariantMarker = "\\d+?\\) ".toRegex()
    val meaningVariantMarker = "\\d+?[).] ".toRegex()
    val exampleVariantMarker = "[а-я][).] ".toRegex()

    val transcription = "\\[t](.+?)\\[/t]".toRegex()
    val translation = "\\[trn](.+?)\\[/trn]".toRegex()
    val reference = "<<(.+?)>>".toRegex()
    val label = "\\[p](.+?)\\[/p]".toRegex()
    val comment = "\\[com](.+?)\\[/com]".toRegex()
    val lang = "\\[lang id=(\\d+)](.+?)\\[/lang]".toRegex()
    val example = "\\[ex](.+?)\\[/ex]".toRegex()
    val stress = "\\['](.)\\[/']".toRegex()

    val sampleVariant = "\\[lang id=(\\d+)](.+?)\\[/lang](.+?(?=\\[lang|\$))".toRegex()

    val partOfSpeech = "\\[p](${PartOfSpeech.labels.joinToString("|")})\\[/p]".toRegex()
    val expressionType = "\\[p](${ExpressionType.labels.joinToString("|")})\\[/p]".toRegex()
    val grammaticalForm = "\\[p](${GrammaticalForm.labels.joinToString("|")})\\[/p]".toRegex()
    val morphemeType = "\\[p](${MorphemeType.labels.joinToString("|")})\\[/p]".toRegex()
}
