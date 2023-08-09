package net.lsafer.enver.test

import net.lsafer.enver.Enver
import net.lsafer.enver.string
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

var invocationCount = 0

@Suppress("ClassName")
data object DOMAIN_OBJECT {
    val PRICE_MEMBER by Enver.string {
        invocationCount++
        it?.toIntOrNull()
    }
}

val DOMAIN_OBJECT.PRICE_EXTENSION by Enver.string {
    invocationCount++
    it?.toIntOrNull()
}

@Suppress("ClassName")
class DOMAIN_CLASS {
    val PRICE_MEMBER by Enver.string {
        invocationCount++
        it?.toIntOrNull()
    }

    override fun toString(): String {
        return "DOMAIN_CLASS"
    }
}

val DOMAIN_CLASS.PRICE_EXTENSION by Enver.string {
    invocationCount++
    it?.toIntOrNull()
}

val PRICE_TOPLEVEL by Enver.string {
    invocationCount++
    it?.toIntOrNull()
}

class EnverPropertyDelegatesTest {
    @Test
    @JsName("_487625")
    fun `member property on an object`() {
        invocationCount = 0

        Enver["DOMAIN_OBJECT.PRICE_MEMBER"] = "1"

        assertEquals(0, invocationCount)

        DOMAIN_OBJECT.PRICE_MEMBER.toString()
        DOMAIN_OBJECT.PRICE_MEMBER.toString()
        DOMAIN_OBJECT.PRICE_MEMBER.toString()

        assertEquals(1, DOMAIN_OBJECT.PRICE_MEMBER)
        assertEquals(1, invocationCount)
    }

    @Test
    @JsName("_418764")
    fun `extension property on an object`() {
        invocationCount = 0

        Enver["DOMAIN_OBJECT.PRICE_EXTENSION"] = "1"

        assertEquals(0, invocationCount)

        DOMAIN_OBJECT.PRICE_EXTENSION.toString()
        DOMAIN_OBJECT.PRICE_EXTENSION.toString()
        DOMAIN_OBJECT.PRICE_EXTENSION.toString()

        assertEquals(1, DOMAIN_OBJECT.PRICE_EXTENSION)
        assertEquals(1, invocationCount)
    }

    @Test
    @JsName("_543157")
    fun `member property on a class`() {
        invocationCount = 0

        val instance = DOMAIN_CLASS()

        assertEquals(0, invocationCount)

        Enver["DOMAIN_CLASS.PRICE_MEMBER"] = "1"

        assertEquals(0, invocationCount)

        instance.PRICE_MEMBER.toString()
        instance.PRICE_MEMBER.toString()
        instance.PRICE_MEMBER.toString()

        assertEquals(1, instance.PRICE_MEMBER)
        assertEquals(1, invocationCount)
    }

    @Test
    @JsName("_763573")
    fun `extension property on a class`() {
        invocationCount = 0

        val instance = DOMAIN_CLASS()

        assertEquals(0, invocationCount)

        Enver["DOMAIN_CLASS.PRICE_EXTENSION"] = "1"

        assertEquals(0, invocationCount)

        instance.PRICE_EXTENSION.toString()
        instance.PRICE_EXTENSION.toString()
        instance.PRICE_EXTENSION.toString()

        assertEquals(1, instance.PRICE_EXTENSION)
        assertEquals(1, invocationCount)
    }

    @Test
    @JsName("_530612")
    fun `toplevel property`() {
        invocationCount = 0

        Enver["PRICE_TOPLEVEL"] = "1"

        assertEquals(0, invocationCount)

        PRICE_TOPLEVEL.toString()
        PRICE_TOPLEVEL.toString()
        PRICE_TOPLEVEL.toString()

        assertEquals(1, PRICE_TOPLEVEL)
        assertEquals(1, invocationCount)
    }
}
