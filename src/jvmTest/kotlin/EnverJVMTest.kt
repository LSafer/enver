package net.lsafer.enver.test

import net.lsafer.enver.EnverJVM
import org.junit.Test
import kotlin.test.assertEquals

class EnverJVMTest {
    @Test
    fun `listeners are removed when not referenced`() {
        val enver = EnverJVM()

        // `run { }` and `({})()`
        // inlines execution which won't make property disappear
        runNoinline {
            val property by enver.createProperty("")
            System.gc()
            assertEquals(1, enver.countListeners())
            // force no optimizations
            property.toString()
        }

        System.gc()

        // size might fail because of caching
        assertEquals(0, enver.countListeners())
    }
}
