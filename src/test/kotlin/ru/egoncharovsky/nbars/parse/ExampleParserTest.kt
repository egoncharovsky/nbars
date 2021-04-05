package ru.egoncharovsky.nbars.parse

import org.junit.jupiter.api.Test
import ru.egoncharovsky.nbars.entity.Example
import ru.egoncharovsky.nbars.utils.SentenceHelper.eng
import ru.egoncharovsky.nbars.utils.SentenceHelper.pt
import kotlin.test.assertEquals

internal class ExampleParserTest {

    @Test
    fun parseMultipleExamplesFromSingle() {
        val raw = RawPart("[lang id=1033]after all[/lang] — а) после, несмотря на; " +
                "[lang id=1033]after all my care the vase was broken[/lang] — несмотря на то, что я был очень осторожен, ваза разбилась; " +
                "[lang id=1033]after all our advice you took that course[/lang] — несмотря на все наши советы, вы так поступили; " +
                "б) в конце концов; всё же; " +
                "[lang id=1033]after all, what does it matter?[/lang] — какое это имеет значение, в конце концов?; " +
                "[lang id=1033]I was right after all![/lang] — всё же я был прав!")
        val parser = ExampleParser()

        val examples = parser.parse(raw)
        assertEquals(
            listOf(
                Example(eng("after all"), pt("после, несмотря на;")),
                Example(eng("after all my care the vase was broken"),
                    pt("несмотря на то, что я был очень осторожен, ваза разбилась;")),
                Example(eng("after all our advice you took that course"),
                    pt("несмотря на все наши советы, вы так поступили;")),
                Example(eng("after all"), pt("в конце концов; всё же;")),
                Example(eng("after all, what does it matter?"), pt("какое это имеет значение, в конце концов?;")),
                Example(eng("I was right after all!"), pt("всё же я был прав!"))
            ),
            examples
        )
    }
}