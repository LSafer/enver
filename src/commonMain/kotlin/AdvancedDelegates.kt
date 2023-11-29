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

/* this whole file has been deprecated  */

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @since 1.0.0
 */
@Deprecated("Use string() instead", ReplaceWith(
    "string(name) { it?.takeIf { it.isNotBlank() } }"
))
fun Enver.contentful(name: String): ReadOnlyProperty<Any?, String?> {
    return string(name) { it?.takeIf { it.isNotBlank() } }
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
@Deprecated("Use string() instead", ReplaceWith(
    "string(name) { block(it?.takeIf { it.isNotBlank() }) }"
))
fun <T> Enver.contentful(name: String, block: (String?) -> T): ReadOnlyProperty<Any?, T> {
    return string(name) { block(it?.takeIf { it.isNotBlank() }) }
}

/**
 * A wrapper over [optional] only accepting
 * contentful strings. (aka. non blank strings)
 *
 * @param name the variable's name.
 * @param default the default value.
 * @since 1.0.0
 */
@Deprecated("Use string() instead", ReplaceWith(
    "string(name) { it?.takeIf { it.isNotBlank() } ?: default }"
))
fun Enver.contentful(name: String, default: String): ReadOnlyProperty<Any?, String> {
    return string(name) { it?.takeIf { it.isNotBlank() } ?: default }
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
@Deprecated("Use string() instead", ReplaceWith(
    "string(name) { block(it?.takeIf { it.isNotBlank() } ?: default) }"
))
fun <T> Enver.contentful(name: String, default: String, block: (String) -> T): ReadOnlyProperty<Any?, T> {
    return string(name) { block(it?.takeIf { it.isNotBlank() } ?: default) }
}

//
/*

@Deprecated("Use ?: operator instead", ReplaceWith("int(name) { it ?: default }"))
fun Enver.int(name: String, default: Int): ReadOnlyProperty<Any?, Int> {
    return int(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("int(name) { block(it ?: default) }"))
fun <T> Enver.int(name: String, default: Int, block: (Int) -> T): ReadOnlyProperty<Any?, T> {
    return int(name) { block(it ?: default) }
}

//
/*

@Deprecated("Use ?: operator instead", ReplaceWith("float(name) { it ?: default }"))
fun Enver.float(name: String, default: Float): ReadOnlyProperty<Any?, Float> {
    return float(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("float(name) { block(it ?: default) }"))
fun <T> Enver.float(name: String, default: Float, block: (Float) -> T): ReadOnlyProperty<Any?, T> {
    return float(name) { block(it ?: default) }
}

//

/*

@Deprecated("Use ?: operator instead", ReplaceWith("double(name) { it ?: default }"))
fun Enver.double(name: String, default: Double): ReadOnlyProperty<Any?, Double> {
    return double(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("double(name) { block(it ?: default) }"))
fun <T> Enver.double(name: String, default: Double, block: (Double) -> T): ReadOnlyProperty<Any?, T> {
    return double(name) { block(it ?: default) }
}

//
/*

@Deprecated("Use ?: operator instead", ReplaceWith("long(name) { it ?: default }"))
fun Enver.long(name: String, default: Long): ReadOnlyProperty<Any?, Long> {
    return long(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("long(name) { block(it ?: default) }"))
fun <T> Enver.long(name: String, default: Long, block: (Long) -> T): ReadOnlyProperty<Any?, T> {
    return long(name) { block(it ?: default) }
}

//

/*
@Deprecated("Use ?: operator instead", ReplaceWith("duration(name) { block(it ?: default) }"))
fun Enver.duration(name: String, default: Duration): ReadOnlyProperty<Any?, Duration> {
    return duration(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("duration(name) { block(it ?: default) }"))
fun <T> Enver.duration(name: String, default: Duration, block: (Duration) -> T): ReadOnlyProperty<Any?, T> {
    return duration(name) { block(it ?: default) }
}

//
/*

@Deprecated("Use ?: operator instead", ReplaceWith("boolean(name) { it ?: default }"))
fun Enver.boolean(name: String, default: Boolean): ReadOnlyProperty<Any?, Boolean> {
    return boolean(name) { it ?: default }
}
*/

@Deprecated("Use ?: operator instead", ReplaceWith("boolean(name) { block(it ?: default) }"))
fun <T> Enver.boolean(name: String, default: Boolean, block: (Boolean) -> T): ReadOnlyProperty<Any?, T> {
    return boolean(name) { block(it ?: default) }
}
