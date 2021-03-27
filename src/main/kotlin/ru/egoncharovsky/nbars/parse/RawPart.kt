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

    fun get(range: IntRange): String {
        val value = raw.substring(range)
        raw.removeRange(range)
        return value
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

    fun getPart(regex: Regex): RawPart {
        return RawPart(get(regex)).also {
            children.add(it)
        }
    }

    fun findPart(regex: Regex): RawPart? {
        return find(regex)?.let { RawPart(it) }?.also {
            children.add(it)
        }
    }

    fun getGroupValues(regex: Regex): List<String> {
        return consumeFindGroups(regex) {
            require(it.size == 1) { "Expected exactly 1 in '$raw' by '$regex' but was ${it.size}: $it" }
        }[0]
    }

    fun findMatchesRange(regex: Regex): List<IntRange> {
        return regex.findAll(raw).map { it.groups[0]!!.range }.toList()
    }

    fun cut(vararg ranges: IntRange): List<RawPart> {
        logger.trace("Cutting by ${ranges.size} ranges")

        val parts = ranges.map {
            logger.trace("Cutting $it from '$raw'")

            RawPart(raw.substring(it))
        }.also {
            children.addAll(it)
        }

        var shift = 0
        ranges.forEach {
            val size = (it.last + 1) - it.first
            val shifted = it.first - shift..it.last - shift

            shift += size
            raw = raw.removeRange(shifted)
        }
        logger.trace("Cut to '$raw'")

        return parts
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

    fun length() = raw.length

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

    private fun consumeFindGroups(regex: Regex, assertion: (List<List<String>>) -> Unit = {}): List<List<String>> {
        return regex.findAll(raw).map { it.groupValues }.toList().also {
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