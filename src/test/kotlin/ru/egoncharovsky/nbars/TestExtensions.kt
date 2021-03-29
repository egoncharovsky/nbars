package ru.egoncharovsky.nbars

import java.io.File

fun Any.getResource(path: String) = File(javaClass.classLoader.getResource(path)!!.toURI())