package ru.egoncharovsky.nbars

object Regexes {
    val headwordMarker = "^[^\\t#]".toRegex()
    val dictionaryEndMarker = "\\{\\{ The End }}".toRegex()

    val marginTag = "\\[/?m\\d?]".toRegex()
    val boldTag = "\\[/?b]".toRegex()
    val italicTag = "\\[/?i]".toRegex()
    val colorTag = "\\[/?c( .*?)?]".toRegex()

    val braces = "[{}]".toRegex()
    val doubleBraces = "\\{\\{|}}".toRegex()
    val squareBrackets = "[\\[\\]]".toRegex()
    val plain = "(.+)".toRegex()

    val homonymMarker = "\\[sup]\\d+?\\[/sup]".toRegex()
    val lexicalGrammarHomonymMarker = "[Ⅰ-Ⅹ]".toRegex()
    val translationMarker = "\\d+?\\.".toRegex()
    val translationVariantMarker = "\\d+?\\)".toRegex()

    val transcription = "\\\\[\\[t](.+?)\\[/t]\\\\]".toRegex()
    val partOfSpeech = "\\[p](n|a)\\[/p]".toRegex()
    val translation = "\\[trn](.+?)\\[/trn]".toRegex()
    val label = "\\[p](.+?)\\[/p]".toRegex()
    val comment = "\\[com](.+?)\\[/com]".toRegex()
    val lang = "\\[lang id=(\\d+)](.+?)\\[/lang]".toRegex()
}
