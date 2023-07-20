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

import net.lsafer.enver.internal.EnverImpl
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias EnverProperty<T> = ReadOnlyProperty<Any?, T>
typealias EnverPropertyProvider<T> = PropertyDelegateProvider<Any?, EnverProperty<T>>

/**
 * A reactive instance holding environment manually
 * loaded variables.
 *
 * Every instance of this class is initialized
 * with nothing and the user is responsible for
 * populating it using either [set] or [plusAssign].
 *
 * @author LSafer
 * @since 1.0.0
 */
interface Enver {
    companion object : Enver by Enver()

    /**
     * Instantly get the current environment
     * variable with the given [name].
     *
     * @since 1.0.0
     */
    operator fun get(name: String): String?

    /**
     * Set the environment variable with the given
     * [name] to the given [value].
     *
     * If the given [value] is null, the
     * environment variable is removed.
     *
     * @since 1.0.0
     */
    operator fun set(name: String, value: String?)

    /**
     * Set the environment variables in the given [source].
     */
    operator fun plusAssign(source: Map<String, String>)

    /**
     * Create a new property that always returns
     * the latest value for [name].
     */
    fun createProperty(name: String): EnverProperty<String?>

    /**
     * Create a new property that always returns
     * the latest value for [name] lazily
     * transformed using [block].
     */
    fun <T> createProperty(name: String, block: (String?) -> T): EnverProperty<T>
}

/**
 * Create a provider that returns a new property
 * using [Enver.createProperty] with the name being
 * the name of the property instance provided to it.
 *
 * If an instance is provided. The following name
 * will be used instead:
 *
 * `"$instance.${property.name}"`
 */
fun Enver.createPropertyProvider(): EnverPropertyProvider<String?> {
    return PropertyDelegateProvider { instance, property ->
        createProperty(inferNameFor(instance, property))
    }
}

/**
 * Create a provider that returns a new property
 * using [Enver.createProperty] with the name being
 * the name of the property instance provided to it.
 *
 * If an instance is provided. The following name
 * will be used instead:
 *
 * `"$instance.${property.name}"`
 */
fun <T> Enver.createPropertyProvider(block: (String?) -> T): EnverPropertyProvider<T> {
    return PropertyDelegateProvider { instance, property ->
        createProperty(inferNameFor(instance, property), block)
    }
}

/**
 * Create a new [Enver] instance.
 */
fun Enver(): Enver {
    return EnverImpl()
}

private fun inferNameFor(instance: Any?, property: KProperty<*>): String {
    return when (instance) {
        null -> property.name
        else -> "$instance.${property.name}"
    }
}
