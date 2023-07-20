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
import net.lsafer.enver.EnverProperty
import java.util.WeakHashMap
import kotlin.properties.ReadOnlyProperty

/**
 * Default [Enver] implementation.
 */
internal class EnverImpl : Enver {
    private val current = mutableMapOf<String, String>()

    /**
     * Flipped map where the keys are the listeners
     * and the values are the environment variable
     * names to listen to.
     */
    private val listeners = WeakHashMap<() -> Unit, String>()

    override fun get(name: String): String? {
        return current[name]
    }

    override fun set(name: String, value: String?) {
        if (value == null)
            current.remove(name)
        else
            current[name] = value

        listeners.forEach { (listener, target) ->
            if (target == name)
                listener()
        }
    }

    override fun plusAssign(source: Map<String, String>) {
        current += source
        listeners.forEach { (listener, target) ->
            if (target in source)
                listener()
        }
    }

    override fun createProperty(name: String): EnverProperty<String?> {
        var value = current[name]
        val listener = { value = current[name] }
        listeners[listener] = name
        return ReadOnlyProperty { _, _ ->
            @Suppress("UNUSED_EXPRESSION")
            listener // you die with me
            value
        }
    }

    override fun <T> createProperty(name: String, block: (String?) -> T): EnverProperty<T> {
        var value = lazy { block(current[name]) }
        val listener = { value = lazy { block(current[name]) } }
        listeners[listener] = name
        return ReadOnlyProperty { _, _ ->
            @Suppress("UNUSED_EXPRESSION")
            listener // you die with me
            value.value
        }
    }
}
