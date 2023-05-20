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
import kotlin.time.Duration

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.contentfulOrNull(): String? {
    return takeIf { it.isNotBlank() }
}

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @since 1.0.0
 */
fun Enver.contentful(name: String): ReadOnlyProperty<Any?, String?> {
    return optional(name) { it?.contentfulOrNull() }
}

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
fun <T> Enver.contentful(name: String, block: (String?) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.contentfulOrNull()) }
}

//

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @param default the default value.
 * @since 1.0.0
 */
fun Enver.contentful(name: String, default: String): ReadOnlyProperty<Any?, String> {
    return optional(name) { it?.contentfulOrNull() ?: default }
}

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @param default the default value.
 * @param block a function for transforming the value to type of [T].
 *               Invoked once on every change of the variable's name.
 * @since 1.0.0
 */
fun <T> Enver.contentful(name: String, default: String, block: (String) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.contentfulOrNull() ?: default) }
}

//

fun Enver.int(name: String): ReadOnlyProperty<Any?, Int?> {
    return optional(name) { it?.toIntOrNull() }
}

fun <T> Enver.int(name: String, block: (Int?) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toIntOrNull()) }
}

//

fun Enver.int(name: String, default: Int): ReadOnlyProperty<Any?, Int> {
    return optional(name) { it?.toIntOrNull() ?: default }
}

fun <T> Enver.int(name: String, default: Int, block: (Int) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toIntOrNull() ?: default) }
}

//

fun Enver.long(name: String): ReadOnlyProperty<Any?, Long?> {
    return optional(name) { it?.toLongOrNull() }
}

fun <T> Enver.long(name: String, block: (Long?) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toLongOrNull()) }
}

//

fun Enver.long(name: String, default: Long): ReadOnlyProperty<Any?, Long> {
    return optional(name) { it?.toLongOrNull() ?: default }
}

fun <T> Enver.long(name: String, default: Long, block: (Long) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toLongOrNull() ?: default) }
}

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toDurationOrNull(): Duration? {
    return Duration.parseOrNull(this)
}

fun Enver.duration(name: String): ReadOnlyProperty<Any?, Duration?> {
    return optional(name) { it?.toDurationOrNull() }
}

fun <T> Enver.duration(name: String, block: (Duration?) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toDurationOrNull()) }
}

//

fun Enver.duration(name: String, default: Duration): ReadOnlyProperty<Any?, Duration> {
    return optional(name) { it?.toDurationOrNull() ?: default }
}

fun <T> Enver.duration(name: String, default: Duration, block: (Duration) -> T): ReadOnlyProperty<Any?, T> {
    return optional(name) { block(it?.toDurationOrNull() ?: default) }
}

//
