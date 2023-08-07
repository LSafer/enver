package net.lsafer.enver.test

import net.lsafer.enver.enverSource
import net.lsafer.enver.internal.generateEnclosureSequenceDoublePointer
import net.lsafer.enver.internal.generateEnclosureSequenceSinglePointer
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress(
    "DotEnvSpaceAroundSeparatorInspection",
    "DotEnvTrailingWhitespaceInspection",
    "DotEnvSpaceInsideNonQuotedInspection"
)
class ParsingTest {
    @Test
    fun `multiline support with single quotes`() {
        val env = enverSource {
            source = """
                NAME=Hassan
                DESCRIPTION="
                Alpha Beta '\"
                Gamma
                "
                JOB=Software Engineer
            """.trimIndent()
        }

        assertEquals(
            env, mapOf(
                "NAME" to "Hassan",
                "DESCRIPTION" to "\nAlpha Beta '\"\nGamma\n",
                "JOB" to "Software Engineer"
            )
        )
    }

    @Test
    fun `comments and whitespaces and blank lines`() {
        val env = enverSource {
            onIncompleteStatement = { true }
            source = """
                NAME=   Hassan  # This is a comment
                #NAME=Jamaan # Abdu
                DESCRIPTION=        "
                Alpha Beta '\"
                Gamma
                "      This is also a #comment
                JOB=   Software Engineer   
                
                OTHER_JOBS
            """.trimIndent()
        }

        assertEquals(
            env, mapOf(
                "NAME" to "Hassan",
                "DESCRIPTION" to "\nAlpha Beta '\"\nGamma\n",
                "JOB" to "Software Engineer",
                "OTHER_JOBS" to ""
            )
        )
    }

    @Test
    fun `escapes and multiple escapes and multiline escapes`() {
        val env = enverSource {
            source = """
                NAME1="\\\\\\\""
                NAME2="\\\\\\""
                NAME3="
                \
                \""
            """.trimIndent()
        }

        assertEquals(
            env, mapOf(
                "NAME1" to "\\\\\\\"",
                "NAME2" to "\\\\\\",
                "NAME3" to "\n\\\n\""
            )
        )
    }

    @Test
    fun `generateEnclosureSequenceSinglePointer check`() {
//        01234567890123456789012
//        { { { } } } { { { } } }
        val openOffsets = sequenceOf(0, 2, 4, 12, 14, 16)
        val closeOffsets = sequenceOf(6, 8, 10, 18, 20, 22)

        val ranges = generateEnclosureSequenceSinglePointer(openOffsets, closeOffsets).toSet()

        val expectedRanges = setOf(
            4..<6,
            2..<8,
            0..<10,
            16..<18,
            14..<20,
            12..<22
        )

        assertEquals(expectedRanges, ranges)
    }

    @Test
    fun `generateEnclosureSequenceDoublePointer check`() {
//        01234567890123456789012
//        { { { } } } { { { } } }
        val openOffsets = sequenceOf(0, 2, 4, 12, 14, 16)
        val closeOffsets = sequenceOf(6, 8, 10, 18, 20, 22)

        val ranges = generateEnclosureSequenceDoublePointer(openOffsets, closeOffsets).toSet()

        val expectedRanges = setOf(
            4..<6,
            2..<8,
            0..<10,
            16..<18,
            14..<20,
            12..<22
        )

        assertEquals(expectedRanges, ranges)
    }
}
