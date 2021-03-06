package ru.egoncharovsky.nbars

import ru.egoncharovsky.nbars.entity.ExpressionType
import ru.egoncharovsky.nbars.entity.GrammaticalForm
import ru.egoncharovsky.nbars.entity.MorphemeType
import ru.egoncharovsky.nbars.entity.PartOfSpeech


object Regexes {

    val endOfLine = "\\eol"

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
    val notTranslationTag = "\\[/?!trs]".toRegex()
    val labelTag = "\\[/?p]".toRegex()

    val braces = "[{}]".toRegex()
    val doubleBraces = "\\{\\{|}}".toRegex()
    val plain = "(.+)".toRegex()
    val dash = "—".toRegex()
    val equal = "=".toRegex()
    val colon = ":".toRegex()
    val squareBrackets = "[\\[\\]]".toRegex()
    val referenceSymbols = "<<|>>".toRegex()

    val escapedSquareBrackets = "(\\\\\\[|\\\\])".toRegex()
    val leftEscapedSquareBracket = "\\\\\\[".toRegex()
    val rightEscapedSquareBracket = "\\\\]".toRegex()

    val letter = "[a-zA-Zа-яА-Я]".toRegex()

    val homonymMarker = "\\[b]\\[sup]\\d+?\\[/sup]\\[/b]".toRegex()
    val sectionMarker = "(?<!\\[c])\\[b][Ⅰ-Ⅹ]\\[/b](?!\\[/c])".toRegex()
    val translationMarker = "(?<=^|\t)\\d+?\\. ".toRegex()
    val translationVariantMarker = "(?<=^|\t)\\d+?\\) ".toRegex()
    val exampleVariantMarker = "[а-я]\\) ".toRegex()
    val idiomMarker = "\\[p]♦\\[/p]".toRegex()

    val transcription = "\\[t](.+?)\\[/t]".toRegex()
    val translation = "\\[trn](.+?)\\[/trn]".toRegex()
    val reference = "<<(.+?)>>".toRegex()
    val label = "\\[p](.+?)\\[/p]".toRegex()
    val comment = "\\[com](.+?)\\[/com]".toRegex()
    val lang = "\\[lang id=(\\d+)](.+?)\\[/lang]".toRegex()
    val example = "\\[ex](.+?)\\[/ex]".toRegex()
    val stress = "\\['](.)\\[/']".toRegex()

    val sampleVariant = "\\[lang id=(\\d+)](.+?)\\[/lang](.+?(?=\\[lang|\$))".toRegex()
    val referencePart = "(от|=)? (<<.+)".toRegex()
    val compound = "\\[trn]\\\\\\[(.+?)\\\\]\\[/trn]".toRegex()
    val idioms = "\\[ex]\\[p]♦\\[/p].+".toRegex()
    val idiomPrefix = "^[ ]*\\[p]♦\\[/p].+".toRegex()

    val partOfSpeech = "\\[p](${PartOfSpeech.labels.joinToString("|")})\\[/p]".toRegex()
    val expressionType = "\\[p](${ExpressionType.labels.joinToString("|")})\\[/p]".toRegex()
    val grammaticalForm = "\\[p](${GrammaticalForm.labels.joinToString("|")})\\[/p]".toRegex()
    val morphemeType = "\\[p](${MorphemeType.labels.joinToString("|")})\\[/p]".toRegex()
}
