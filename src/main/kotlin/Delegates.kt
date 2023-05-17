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

import kotlin.properties.ReadOnlyProperty

// require

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The returned property will throw an exception
 * if accessed while not populated.
 *
 * @param name the variable's name.
 * @since 1.0.0
 */
fun Enver.required(name: String): ReadOnlyProperty<Any?, String> {
    return required(name) { it }
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The returned property will throw an exception
 * if accessed while not populated.
 *
 * @param name the variable's name.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
fun <T> Enver.required(name: String, block: (String) -> T): ReadOnlyProperty<Any?, T> {
    return this.optional(name) {
        it ?: error("Required environment variable uninitialized: $name")
        block(it)
    }
}

// return null

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to `null`
 *
 * @param name the variable's name.
 * @since 1.0.0
 */
fun Enver.optional(name: String): ReadOnlyProperty<Any?, String?> {
    return optional(name) { it }
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to `null`
 *
 * @param name the variable's name.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
@OptIn(ExperimentalEnverApi::class)
fun <T> Enver.optional(name: String, block: (String?) -> T): ReadOnlyProperty<Any?, T> {
    var current = lazy { block(null) }
    subscribe(name) {
        current = lazy { block(it) }
    }
    return ReadOnlyProperty { _, _ ->
        current.value
    }
}

// with default

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to [default].
 *
 * @param name the variable's name.
 * @param default the default value.
 * @since 1.0.0
 */
fun Enver.optional(name: String, default: String): ReadOnlyProperty<Any?, String> {
    return optional(name, default) { it }
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to [default].
 *
 * @param name the variable's name.
 * @param default the default value.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
@OptIn(ExperimentalEnverApi::class)
fun <T> Enver.optional(name: String, default: String, block: (String) -> T): ReadOnlyProperty<Any?, T> {
    var current = lazy { block(default) }
    subscribe(name) {
        current = lazy { block(it) }
    }
    return ReadOnlyProperty { _, _ ->
        current.value
    }
}

// DEPRECATED

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to [default].
 *
 * @param name the variable's name.
 * @param default the default value.
 * @since 1.0.0
 */
@Deprecated("Use optional() instead", ReplaceWith("optional(name, default)"))
operator fun Enver.invoke(name: String, default: String): ReadOnlyProperty<Any?, String> {
    return optional(name, default)
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to [default].
 *
 * @param name the variable's name.
 * @param default the default value.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
@Deprecated("Use optional() instead", ReplaceWith("optional(name, default, block)"))
operator fun <T> Enver.invoke(name: String, default: String, block: (String) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name, default, block)
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to `null`
 *
 * @param name the variable's name.
 * @since 1.0.0
 */
@Deprecated("Use optional() instead", ReplaceWith("optional(name)"))
operator fun Enver.invoke(name: String): ReadOnlyProperty<Any?, String?> {
    return optional(name)
}

/**
 * Obtain a property that uses this enver instance
 * as the source of truth.
 *
 * The source is initially set to `null`
 *
 * @param name the variable's name.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
@Deprecated("Use optional() instead", ReplaceWith("optional(name, block)"))
operator fun <T> Enver.invoke(name: String, block: (String?) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name, block)
}
