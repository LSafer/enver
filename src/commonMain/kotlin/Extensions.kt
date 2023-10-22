/*
 *	Copyright 2023 cufy.org and meemer.com
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

import kotlinx.datetime.Instant
import kotlin.time.Duration

//

fun Enver.string(): EnverPropertyProvider<String?> {
    return createPropertyProvider()
}

fun <T> Enver.string(block: (String?) -> T): EnverPropertyProvider<T> {
    return createPropertyProvider(block)
}

fun Enver.string(name: String): EnverProperty<String?> {
    return createProperty(name)
}

fun <T> Enver.string(name: String, block: (String?) -> T): EnverProperty<T> {
    return createProperty(name, block)
}

//

/** @see toBooleanStrictOrNull */
fun Enver.boolean(): EnverPropertyProvider<Boolean?> {
    return string { it?.toBooleanStrictOrNull() }
}

/** @see toBooleanStrictOrNull */
fun <T> Enver.boolean(block: (Boolean?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toBooleanStrictOrNull()) }
}

/** @see toBooleanStrictOrNull */
fun Enver.boolean(name: String): EnverProperty<Boolean?> {
    return string(name) { it?.toBooleanStrictOrNull() }
}

/** @see toBooleanStrictOrNull */
fun <T> Enver.boolean(name: String, block: (Boolean?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toBooleanStrictOrNull()) }
}

//

/** @see toIntOrNull */
fun Enver.int(): EnverPropertyProvider<Int?> {
    return string { it?.toIntOrNull() }
}

/** @see toIntOrNull */
fun <T> Enver.int(block: (Int?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toIntOrNull()) }
}

/** @see toIntOrNull */
fun Enver.int(name: String): EnverProperty<Int?> {
    return string(name) { it?.toIntOrNull() }
}

/** @see toIntOrNull */
fun <T> Enver.int(name: String, block: (Int?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toIntOrNull()) }
}

//

/** @see toLongOrNull */
fun Enver.long(): EnverPropertyProvider<Long?> {
    return string { it?.toLongOrNull() }
}

/** @see toLongOrNull */
fun <T> Enver.long(block: (Long?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toLongOrNull()) }
}

/** @see toLongOrNull */
fun Enver.long(name: String): EnverProperty<Long?> {
    return string(name) { it?.toLongOrNull() }
}

/** @see toLongOrNull */
fun <T> Enver.long(name: String, block: (Long?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toLongOrNull()) }
}

//

/** @see toFloatOrNull */
fun Enver.float(): EnverPropertyProvider<Float?> {
    return string { it?.toFloatOrNull() }
}

/** @see toFloatOrNull */
fun <T> Enver.float(block: (Float?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toFloatOrNull()) }
}

/** @see toFloatOrNull */
fun Enver.float(name: String): EnverProperty<Float?> {
    return string(name) { it?.toFloatOrNull() }
}

/** @see toFloatOrNull */
fun <T> Enver.float(name: String, block: (Float?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toFloatOrNull()) }
}

//

/** @see toDoubleOrNull */
fun Enver.double(): EnverPropertyProvider<Double?> {
    return string { it?.toDoubleOrNull() }
}

/** @see toDoubleOrNull */
fun <T> Enver.double(block: (Double?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toDoubleOrNull()) }
}

/** @see toDoubleOrNull */
fun Enver.double(name: String): EnverProperty<Double?> {
    return string(name) { it?.toDoubleOrNull() }
}

/** @see toDoubleOrNull */
fun <T> Enver.double(name: String, block: (Double?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toDoubleOrNull()) }
}

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toDurationOrNull(): Duration? {
    return Duration.parseOrNull(this)
}

/** @see Duration.parseOrNull */
fun Enver.duration(): EnverPropertyProvider<Duration?> {
    return string { it?.toDurationOrNull() }
}

/** @see Duration.parseOrNull */
fun <T> Enver.duration(block: (Duration?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toDurationOrNull()) }
}

/** @see Duration.parseOrNull */
fun Enver.duration(name: String): EnverProperty<Duration?> {
    return string(name) { it?.toDurationOrNull() }
}

/** @see Duration.parseOrNull */
fun <T> Enver.duration(name: String, block: (Duration?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toDurationOrNull()) }
}

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toInstantOrNull(): Instant? {
    return runCatching { Instant.parse(this) }.getOrNull()
}

/** @see Instant.parse */
fun Enver.instant(): EnverPropertyProvider<Instant?> {
    return string { it?.toInstantOrNull() }
}

/** @see Instant.parse */
fun <T> Enver.instant(block: (Instant?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toInstantOrNull()) }
}

/** @see Instant.parse */
fun Enver.instant(name: String): EnverProperty<Instant?> {
    return string(name) { it?.toInstantOrNull() }
}

/** @see Instant.parse */
fun <T> Enver.instant(name: String, block: (Instant?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toInstantOrNull()) }
}
