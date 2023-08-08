package net.lsafer.enver.test

import net.lsafer.enver.Enver
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EnverTest {
    @Test
    @JsName("_288644")
    fun `createProperty returns live data`() {
        val enver = Enver()
        val name by enver.createProperty("NAME")

        assertNull(name)

        enver["NAME"] = "alpha"

        assertEquals(name, "alpha")

        enver["NAME"] = "beta"

        assertEquals(name, "beta")
    }

    @Test
    @JsName("_196159")
    fun `createProperty transforms applied lazily`() {
        var invocationCount = 0
        val enver = Enver()
        val price by enver.createProperty("PRICE") {
            invocationCount++
            it?.toIntOrNull()
        }

        assertEquals(0, invocationCount)

        enver["PRICE"] = "1"

        assertEquals(0, invocationCount)

        price.toString()
        price.toString()
        price.toString()

        assertEquals(1, invocationCount)
    }
}
