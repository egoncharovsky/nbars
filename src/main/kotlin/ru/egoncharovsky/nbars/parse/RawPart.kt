package ru.egoncharovsky.nbars.parse

import mu.KotlinLogging
import ru.egoncharovsky.nbars.exception.EmptyRaw
import ru.egoncharovsky.nbars.exception.FailExpectation
import ru.egoncharovsky.nbars.exception.FinishFail

data class RawPart(
    private var raw: String
) {
    private val logger = KotlinLogging.logger { }

    private val children = mutableSetOf<RawPart>()

    fun get(regex: Regex, group: Int = 1): String {
        return consumeFind(regex, group) {
            require(it.size == 1) { FailExpectation("Expected exactly 1", raw, regex, it.size, it) }
        }[0]
    }

    fun get(range: IntRange): String {
        val value = raw.substring(range)
        raw.removeRange(range)
        return value
    }

    fun getAll(regex: Regex, group: Int = 1): List<String> {
        return consumeFind(regex, group) {
            require(it.isNotEmpty()) { FailExpectation("Expected at least 1", raw, regex, it.size, it) }
        }
    }

    fun find(regex: Regex, group: Int = 1): String? {
        return consumeFind(regex, group) {
            require(it.size <= 1) { FailExpectation("No more than 1", raw, regex, it.size, it) }
        }.getOrNull(0)
    }

    fun findAll(regex: Regex, group: Int = 1): List<String> {
        return consumeFind(regex, group)
    }

    fun findBefore(regex: Regex, before: Regex, group: Int = 1): String? {
        return consumeFindBefore(regex, before, group) {
            require(it.size <= 1) { FailExpectation("No more than 1", raw, regex, it.size, it) }
        }.getOrNull(0)
    }

    fun remove(regex: Regex): RawPart {
        find(regex, 0)
        return this
    }

    fun removeAll(regex: Regex): RawPart {
        findAll(regex, 0)
        return this
    }

    fun getPart(regex: Regex, group: Int = 1): RawPart {
        return addPart(get(regex, group))
    }

    fun findPart(regex: Regex, group: Int = 1): RawPart? {
        return find(regex, group)?.let { addPart(it) }
    }

    fun findPartBefore(regex: Regex, before: Regex, group: Int = 1): RawPart? {
        return findBefore(regex, before, group)?.let { addPart(it) }
    }

    fun findAllParts(regex: Regex, group: Int = 1): List<RawPart> {
        return findAll(regex, group).map { addPart(it) }
    }

    fun getGroupValues(regex: Regex): List<String> {
        return consumeFindGroups(regex) {
            require(it.size == 1) { FailExpectation("Expected exactly 1", raw, regex, it.size, it) }
        }[0]
    }

    fun findMatchesRange(regex: Regex): List<IntRange> {
        return regex.findAll(raw).map { it.groups[0]!!.range }.toList()
    }

    fun cut(vararg ranges: IntRange): List<RawPart> {
        logger.trace("Cutting by ${ranges.size} ranges")

        val parts = ranges.map {
            logger.trace("Cutting $it from '$raw'")

            addPart(raw.substring(it))
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
        require(raw.isNotEmpty()) { EmptyRaw("split") }

        return raw.split(regex).map { it.trim() }.map { addPart(it) }.also {
            raw = ""
        }
    }

    fun before(regex: Regex): Pair<RawPart, RawPart?> {
        logger.trace("Consuming before by '$regex' of '$raw'")
        require(raw.isNotEmpty()) { EmptyRaw("take before") }

        val (before, after) = before(raw, regex)

        return if (after != null) {
            logger.trace("Divided by '$regex' into before:'$before' and after'$after'")

            raw = ""
            addPart(before) to addPart(after)
        } else {
            logger.trace("No occurrences found of '$regex' returning itself: '$raw'")

            this to null
        }
    }

    fun length() = raw.length

    private fun finish() {
        require(raw.isEmpty()) { FinishFail("empty raw", raw) }
    }

    fun finishAll() {
        finish()
        children.forEach { it.finishAll() }
    }

    private fun consumeFind(regex: Regex, group: Int, assertion: (List<String>) -> Unit = {}): List<String> {
        return findAll(raw, regex, group).also {
            assertion(it)
            raw = consume(raw, regex)
        }
    }

    private fun consumeFindBefore(regex: Regex, before: Regex, group: Int = 1, assertion: (List<String>) -> Unit = {}): List<String> {
        val (rawBefore, rawAfter) = before(raw, before)
        return findAll(rawBefore, regex, group).also {
            assertion(it)
            val beforeCut = consume(rawBefore, regex)
            raw = beforeCut + rawAfter.orEmpty()
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

    private fun consume(raw: String,regex: Regex): String {
        logger.trace("Consuming of '$raw'")
        return raw.replace(regex, "").trim().also {
            logger.trace("Raw cut to '$it'")
        }
    }

    private fun findAll(raw: String, regex: Regex, group: Int): List<String> {
       return regex.findAll(raw).map { it.groupValues[group] }.toList().also {
           logger.trace("Found ${it.size} matches for '$regex' in '$raw'")
       }
    }

    private fun before(raw: String, regex: Regex): Pair<String, String?> {
        return regex.find(raw)?.let {
            val first = it.groups[0]!!.range.first

            raw.substring(0 until first) to raw.substring(first until raw.length)
        } ?: run {
            raw to null
        }
    }

    private fun addPart(value: String): RawPart {
        logger.trace("Register children $value of $raw")
        return RawPart(value).also { children.add(it) }
    }

    private fun require(requirement: Boolean, exception: () -> Exception) {
        if (!requirement) throw exception.invoke()
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