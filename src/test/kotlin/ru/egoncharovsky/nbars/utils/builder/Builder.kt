package ru.egoncharovsky.nbars.utils.builder

interface Builder<T> {
    fun build(): T
}