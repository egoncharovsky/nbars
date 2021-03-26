package ru.egoncharovsky.nbars

import java.io.File

fun Any.readResourceLines(path: String) = File(javaClass.classLoader.getResource(path)!!.toURI()).readLines()