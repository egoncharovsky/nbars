package ru.egoncharovsky.nbars.entity.text

class Sentence(parts: List<TextPart>) : Text {
    val parts: List<TextPart> = normalize(parts)

    override fun asPlain(): String = parts.joinToString("") { it.asPlain() }

    private fun normalize(parts: List<TextPart>): List<TextPart> {
        return parts.fold(mutableListOf()) { combined: MutableList<TextPart>, textPart: TextPart ->
            val previous = combined.lastOrNull()
            if (previous is PlainText && textPart is PlainText) {
                combined[combined.lastIndex] = previous.merge(textPart)
            } else {
                combined.add(textPart)
            }
            combined
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Sentence) return false

        if (parts != other.parts) return false

        return true
    }

    override fun hashCode(): Int {
        return parts.hashCode()
    }

    override fun toString(): String {
        return "Sentence(parts=$parts)"
    }


    companion object {
        fun join(texts: Collection<Text>, separator: String): Text? {
            if (texts.isEmpty()) return null
            val parts = texts
                .map {
                    when (it) {
                        is Sentence -> it.parts
                        is TextPart -> listOf(it)
                        else -> throw IllegalArgumentException("Unsupported text type of $it")
                    }
                }
                .reduce { acc, list -> acc.plus(PlainText(separator)).plus(list) }
            return textFrom(parts)
        }

        fun textFrom(parts: List<TextPart>) = if (parts.size > 1) {
            Sentence(parts)
        } else {
            parts[0]
        }
    }
}