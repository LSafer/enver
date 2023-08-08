package net.lsafer.enver.test

import js.core.FinalizationRegistry
import js.core.JsTuple1
import js.core.globalThis
import js.core.tupleOf
import kotlinx.coroutines.suspendCancellableCoroutine
import net.lsafer.enver.EnverJS
import kotlin.coroutines.resume
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration

/** runs [block] immediately */
internal suspend fun <R> runSuspendNoinline(block: suspend () -> R): R {
    return block()
}

/** Count the number of registered listeners. */
internal fun EnverJS.countListeners(): Int {
    // force no optimizations
    return listeners.map { 1 }.sum()
}

/** wrapper over javascript `setTimeout` */
internal fun setTimeout(duration: Duration = Duration.ZERO, callback: () -> Unit) {
    val millis = duration.inWholeMilliseconds
    js("setTimeout(callback, millis)")
}

internal fun suggestGC() {
    val gc = globalThis.gc

    when (jsTypeOf(gc)) {
        "function" -> gc()
        else -> error("Cannot force GC to work. Please add `--expose_gc` flag when starting tests.")
    }
}

/** force the GC to execute then run [then]. */
internal fun forceGC(then: () -> Unit) {
    var done = false
    val sizes = listOf(5_000, 50_000, 500_000, 5_000_000)
    val remaining = mutableListOf<JsTuple1<Int>>()

    val registry = FinalizationRegistry<JsTuple1<Int>> {
        remaining.remove(it)

        if (remaining.isEmpty()) {
            if (!done) {
                done = true
                then()
            }
        }
    }

    fun append(size: Int) {
        val array = Array(size) {
            { Random.nextInt() }
        }
        val reference = tupleOf(size)
        remaining.add(reference)
        registry.register(array, reference)
        suggestGC()
    }

    repeat(10) {
        append(Random.nextInt(5_000..<2_000_000))
    }

    fun hit() {
        if (done) return
        append(Random.nextInt(5_000..<2_000_000))
        setTimeout { hit() }
    }

    hit()
}

internal suspend fun forceGC() {
    repeat(10) {
        suspendCancellableCoroutine { continuation ->
            forceGC { continuation.resume(Unit) }
        }
    }
}
