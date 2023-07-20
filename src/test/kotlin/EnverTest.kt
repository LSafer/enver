package net.lsafer.enver.test

import net.lsafer.enver.Enver
import net.lsafer.enver.internal.EnverImpl
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import kotlin.test.assertNull

object EnverTest {
    @Test
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

    @Test
    fun `EnverImpl removes the property listener when not referenced`() {
        val enver = EnverImpl()

        run {
            val property by enver.createProperty("")
            assertEquals(1, enver.countListeners())
            // force no optimizations
            property.toString()
        }

        System.gc()

        // size might fail because of caching
        assertEquals(0, enver.countListeners())
    }
}

private fun EnverImpl.countListeners(): Int {
    val listenersProperty = EnverImpl::class.memberProperties.first {
        it.name == "listeners"
    }
    listenersProperty.isAccessible = true
    val listeners = listenersProperty.get(this) as Map<*, *>
    // force no optimizations
    return listeners.map { 1 }.sum()
}
