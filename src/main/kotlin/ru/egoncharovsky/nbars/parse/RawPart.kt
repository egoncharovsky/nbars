package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging

data class RawPart(
    private var raw: String
) {
    private val logger = KotlinLogging.logger { }

    private var children = mutableListOf<RawPart>()

    fun get(regex: Regex): String {
        return consumeFind(regex) {
            require(it.size == 1) { "Expected exactly 1 in '$raw' by '$regex' but was ${it.size}: $it" }
        }[0]
    }

    fun getAll(regex: Regex): List<String> {
        return consumeFind(regex) {
            require(it.isNotEmpty()) { "Expected at least 1 in '$raw' by '$regex' but was ${it.size}: $it" }
        }
    }

    fun find(regex: Regex): String? {
        return consumeFind(regex) {
            require(it.size <= 1) { "No more than 1 expected in '$raw' by '$regex' but was ${it.size}: $it" }
        }.getOrNull(0)
    }

    fun findAll(regex: Regex): List<String> {
        return consumeFind(regex)
    }

    fun all(): String? {
        logger.trace("Getting all of $raw")
        return if (raw.isNotEmpty()) {
            val all = raw
            raw = ""
            all
        } else null
    }

    fun split(regex: Regex): List<RawPart> {
        logger.trace("Consuming split by '$regex' of '$raw'")
        require(raw.isNotEmpty()) { "Try to split empty raw" }

        return raw.split(regex).map { it.trim() }.map { RawPart(it) }.also {
            children.addAll(it)
            raw = ""
        }
    }

    fun before(regex: Regex): Pair<RawPart, RawPart?> {
        logger.trace("Consuming before by '$regex' of '$raw'")
        require(raw.isNotEmpty()) { "Try to take before from empty raw" }

        return regex.find(raw)?.let {
            val first = it.groups[0]!!.range.first

            val before = RawPart(raw.substring(0 until first))
            val after = RawPart(raw.substring(first until raw.length))

            children.add(before)
            children.add(after)
            raw = ""

            logger.trace("Divided by '$regex' into before:'$before' and after'$after'")

            Pair(before, after)
        } ?: kotlin.run {
            logger.trace("No occurrences found of '$regex' returning itself: '$raw'")
            Pair(this, null)
        }
    }

//    fun divide(regex: Regex, into: Int): List<RawPart> {
//        logger.trace("Consuming divide into $into parts by '$regex' of '$raw'")
//        require(raw.isNotEmpty()) { "Try to divide empty raw" }
//
//        val split = raw.split(regex)
//        val occurrences = regex.findAll(raw).map { it.groupValues[0] }.toList()
//
//        if (occurrences.isEmpty()) {
//            logger.trace("No occurrences found")
//            return listOf(this)
//        }
//
//        val divided = (0 until into).map { i ->
//            when (i % 2) {
//                0 -> split.getOrElse(i / 2) { "" }
//                1 -> occurrences.getOrElse(i / 2) { "" }
//                else -> throw IllegalStateException("Unreachable state")
//            }
//        }
//
//        return divided.map { it.trim() }.map { RawPart(it) }.also {
//            children.addAll(it)
//            raw = ""
//        }
//    }

    fun finish() {
        require(raw.isEmpty()) { "Finish: expected empty raw but not: '$raw'" }
    }

    fun finishAll() {
        finish()
        children.forEach { it.finishAll() }
    }

    private fun consumeFind(regex: Regex, assertion: (List<String>) -> Unit = {}): List<String> {
        return regex.findAll(raw).map { it.groupValues[1] }.toList().also {
            logger.trace("Found ${it.size} matches for '$regex'")
            assertion(it)

            logger.trace("Consuming of '$raw'")
            raw = raw.replace(regex, "").trim()
            logger.trace("Raw cut to '$raw'")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawPart) return false

        if (raw != other.raw) return false

        return true
    }

    override fun hashCode(): Int {
        return raw.hashCode()
    }

    override fun toString(): String {
        return "RawPart(raw='$raw', children=$children)"
    }
}