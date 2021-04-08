package ru.egoncharovsky.nbars.utils.builder.article

import ru.egoncharovsky.nbars.utils.builder.Builder

abstract class ArticleBuilder<A, S, SB : Builder<List<S>>, B : ArticleBuilder<A, S, SB, B>> : Builder<A> {
    protected val homonyms = mutableListOf<List<S>>()

    fun homonyms(applyParams: (SB) -> Unit): B {
        homonyms.add(sectionsBuilder().also(applyParams).build())
        return builder()
    }

    protected abstract fun sectionsBuilder(): SB

    protected abstract fun builder(): B
}