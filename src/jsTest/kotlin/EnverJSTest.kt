package net.lsafer.enver.test

import kotlinx.coroutines.test.runTest
import net.lsafer.enver.EnverJS
import kotlin.test.Test
import kotlin.test.assertEquals

class EnverJSTest {
    @Test
    @JsName("_272095")
    fun `listeners are removed when not referenced`() = runTest {
        val enver = EnverJS()

        // `run { }` and `({})()`
        // inlines execution which won't make property disappear
        runSuspendNoinline {
            val property by enver.createProperty("")
            forceGC()
            assertEquals(1, enver.countListeners())
            // force no optimizations
            property.toString()
        }

        forceGC()

        // size might fail because of caching
        assertEquals(0, enver.countListeners())
    }
}
