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

import java.util.*
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.jvm.jvmErasure

internal class EnverJVM : Enver {
    private val current = mutableMapOf<String, String>()

    /**
     * Flipped map where the keys are the listeners
     * and the values are the environment variable
     * names to listen to.
     */
    internal val listeners = WeakHashMap<() -> Unit, String>()

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

actual fun Enver(): Enver {
    return EnverJVM()
}

actual fun Enver.createProperty(): EnverProperty<String?> {
    var k: Pair<Any?, KProperty<Any?>>? = null
    lateinit var p: EnverProperty<String?>
    return EnverProperty { instance, property ->
        val keys = instance to property

        if (k == null) {
            val name = inferNameFor(instance, property)
            name ?: error("Could not infer name for ${instance}.${property.name}")
            k = keys
            p = createProperty(name)
        } else if (k != keys) {
            error("Enver.createProperty() does not yet support being used multiple times")
        }

        p.getValue(instance, property)
    }
}

actual fun <T> Enver.createProperty(block: (String?) -> T): EnverProperty<T> {
    var k: Pair<Any?, KProperty<Any?>>? = null
    lateinit var p: EnverProperty<T>
    return ReadOnlyProperty { instance, property ->
        val keys = instance to property

        if (k == null) {
            val name = inferNameFor(instance, property)
            name ?: error("Could not infer name for ${instance}.${property.name}")
            k = keys
            p = createProperty(name, block)
        } else if (k != keys) {
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
        val objectInstance = property.extensionReceiverParameter!!.type.jvmErasure.objectInstance

        objectInstance ?: return null

        return "$objectInstance.${property.name}"
    }

    return property.name
}
