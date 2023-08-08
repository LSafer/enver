package net.lsafer.enver.test

import net.lsafer.enver.EnverJVM

internal fun runNoinline(block: () -> Unit) {
    block()
}

internal fun EnverJVM.countListeners(): Int {
    // force no optimizations
    return listeners.map { 1 }.sum()
}
