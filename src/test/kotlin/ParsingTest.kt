package net.lsafer.enver.test

import net.lsafer.enver.enverSource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

object ParsingTest {
    @Test
    fun `multiline support with single quotes`() {
        val env = enverSource("""
            NAME=Hassan
            DESCRIPTION="
            Alpha Beta '\"
            Gamma
            "
            JOB=Software Engineer
        """.trimIndent())

        assertEquals(env, mapOf(
            "NAME" to "Hassan",
            "DESCRIPTION" to "\nAlpha Beta '\"\nGamma\n",
            "JOB" to "Software Engineer"
        ))
    }

    @Test
    fun `comments and whitespaces and blank lines`() {
        val env = enverSource("""
            NAME=   Hassan  # This is a comment
            #NAME=Jamaan # Abdu
            DESCRIPTION=        "
            Alpha Beta '\"
            Gamma
            "      This is also a #comment
            JOB=   Software Engineer   
            
            OTHER_JOBS
        """.trimIndent())

        assertEquals(env, mapOf(
            "NAME" to "Hassan",
            "DESCRIPTION" to "\nAlpha Beta '\"\nGamma\n",
            "JOB" to "Software Engineer",
            "OTHER_JOBS" to ""
        ))
    }

    @Test
    fun `escapes and multiple escapes and multiline escapes`() {
        val env = enverSource("""
            NAME1="\\\\\\\""
            NAME2="\\\\\\""
            NAME3="
            \
            \""
        """.trimIndent())

        assertEquals(env, mapOf(
            "NAME1" to "\\\\\\\"",
            "NAME2" to "\\\\\\",
            "NAME3" to "\n\\\n\""
        ))
    }
}
