/*
 *	Copyright 2023 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package net.lsafer.enver.internal

import net.lsafer.enver.Enver
import net.lsafer.enver.ExperimentalEnverApi
import net.lsafer.enver.InternalEnverApi

/**
 * Default [Enver] implementation.
 */
@InternalEnverApi
open class EnverImpl : Enver {
    private val current = mutableMapOf<String, String>()
    private val listeners = mutableMapOf<Any, MutableList<() -> Unit>>()

    override fun get(name: String): String? {
        return current[name]
    }

    override fun set(name: String, value: String?) {
        if (value == null)
            current.remove(name)
        else
            current[name] = value

        listeners.forEach { it.value.forEach { it() } }
    }

    override fun plusAssign(source: Map<String, String>) {
        current += source
        listeners.forEach { it.value.forEach { it() } }
    }

    @ExperimentalEnverApi
    override fun subscribe(block: (Map<String, String>) -> Unit, key: Any) {
        listeners.getOrPut(key) { mutableListOf() } += { block(current) }
        block(current)
    }

    @ExperimentalEnverApi
    override fun unsubscribe(key: Any) {
        listeners.remove(key)
    }
}
