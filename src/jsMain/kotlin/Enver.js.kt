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
package net.lsafer.enver

import js.core.FinalizationRegistry
import js.core.WeakRef
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

internal class EnverJS : Enver {
    private val current = mutableMapOf<String, String>()

    /**
     * Flipped map where the keys are the listeners
     * and the values are the environment variable
     * names to listen to.
     */
    internal val listeners = mutableMapOf<WeakRef<() -> Unit>, String>()

    private val registry = FinalizationRegistry<WeakRef<() -> Unit>> {
        val iterator = listeners.iterator()

        for ((ref) in iterator) {
            if (ref === it) {
                iterator.remove()
                break
            }
        }
    }

    override fun get(name: String): String? {
        return current[name]
    }

    override fun set(name: String, value: String?) {
        if (value == null)
            current.remove(name)
        else
            current[name] = value

        listeners.forEach { (listenerRef, target) ->
            val listener = listenerRef.deref() ?: return@forEach

            if (target == name)
                listener()
        }
    }

    override fun plusAssign(source: Map<String, String>) {
        current += source
        listeners.forEach { (listenerRef, target) ->
            val listener = listenerRef.deref() ?: return@forEach

            if (target in source)
                listener()
        }
    }

    override fun asMap(): Map<String, String> {
        // fixme: return actually unmodifiable map
        return current
    }

    override fun createProperty(name: String): EnverProperty<String?> {
        var value = current[name]
        val listener = { value = current[name] }
        val reference = WeakRef(listener)
        registry.register(listener, reference)
        listeners[reference] = name
        return ReadOnlyProperty { _, _ ->
            listener.toString() // you die with me
            value
        }
    }

    override fun <T> createProperty(name: String, block: (String?) -> T): EnverProperty<T> {
        var value = lazy { block(current[name]) }
        val listener = { value = lazy { block(current[name]) } }
        val reference = WeakRef(listener)
        registry.register(listener, reference)
        listeners[reference] = name
        return ReadOnlyProperty { _, _ ->
            listener.toString() // you die with me
            value.value
        }
    }
}

actual fun Enver(): Enver {
    return EnverJS()
}

actual fun Enver.createProperty(): EnverProperty<String?> {
    var n: String? = null
    lateinit var p: EnverProperty<String?>
    return EnverProperty { instance, property ->
        val name = inferNameFor(instance, property)

        if (n == null) {
            name ?: error("Could not infer name for ${instance}.${property.name}")
            n = name
            p = createProperty(name)
        } else if (n != name) {
            error("Enver.createProperty() does not yet support being used multiple times")
        }

        p.getValue(instance, property)
    }
}

actual fun <T> Enver.createProperty(block: (String?) -> T): EnverProperty<T> {
    var n: String? = null
    lateinit var p: EnverProperty<T>
    return ReadOnlyProperty { instance, property ->
        val name = inferNameFor(instance, property)

        if (n == null) {
            name ?: error("Could not infer name for ${instance}.${property.name}")
            n = name
            p = createProperty(name, block)
        } else if (n != name) {
            error("Enver.createProperty() does not yet support being used multiple times")
        }

        p.getValue(instance, property)
    }
}

actual fun Enver.createPropertyProvider(): EnverPropertyProvider<String?> {
    return PropertyDelegateProvider { instance, property ->
        val name = inferNameFor(instance, property)
        if (name == null) createProperty()
        else createProperty(name)
    }
}

actual fun <T> Enver.createPropertyProvider(block: (String?) -> T): EnverPropertyProvider<T> {
    return PropertyDelegateProvider { instance, property ->
        val name = inferNameFor(instance, property)
        if (name == null) createProperty(block)
        else createProperty(name, block)
    }
}

private fun inferNameFor(instance: Any?, property: KProperty<*>): String? {
    if (instance != null) {
        return "$instance.${property.name}"
    }

    if (property is KProperty1<*, *>) {
        return null
    }

    return property.name
}
