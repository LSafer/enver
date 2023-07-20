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

fun Enver.boolean(): EnverPropertyProvider<Boolean?> {
    return string { it?.toBooleanStrictOrNull() }
}

fun <T> Enver.boolean(block: (Boolean?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toBooleanStrictOrNull()) }
}

fun Enver.boolean(name: String): EnverProperty<Boolean?> {
    return string(name) { it?.toBooleanStrictOrNull() }
}

fun <T> Enver.boolean(name: String, block: (Boolean?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toBooleanStrictOrNull()) }
}

//

fun Enver.int(): EnverPropertyProvider<Int?> {
    return string { it?.toIntOrNull() }
}

fun <T> Enver.int(block: (Int?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toIntOrNull()) }
}

fun Enver.int(name: String): EnverProperty<Int?> {
    return string(name) { it?.toIntOrNull() }
}

fun <T> Enver.int(name: String, block: (Int?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toIntOrNull()) }
}

//

fun Enver.long(): EnverPropertyProvider<Long?> {
    return string { it?.toLongOrNull() }
}

fun <T> Enver.long(block: (Long?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toLongOrNull()) }
}

fun Enver.long(name: String): EnverProperty<Long?> {
    return string(name) { it?.toLongOrNull() }
}

fun <T> Enver.long(name: String, block: (Long?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toLongOrNull()) }
}

//

fun Enver.float(): EnverPropertyProvider<Float?> {
    return string { it?.toFloatOrNull() }
}

fun <T> Enver.float(block: (Float?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toFloatOrNull()) }
}

fun Enver.float(name: String): EnverProperty<Float?> {
    return string(name) { it?.toFloatOrNull() }
}

fun <T> Enver.float(name: String, block: (Float?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toFloatOrNull()) }
}

//

fun Enver.double(): EnverPropertyProvider<Double?> {
    return string { it?.toDoubleOrNull() }
}

fun <T> Enver.double(block: (Double?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toDoubleOrNull()) }
}

fun Enver.double(name: String): EnverProperty<Double?> {
    return string(name) { it?.toDoubleOrNull() }
}

fun <T> Enver.double(name: String, block: (Double?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toDoubleOrNull()) }
}

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toDurationOrNull(): Duration? {
    return Duration.parseOrNull(this)
}

fun Enver.duration(): EnverPropertyProvider<Duration?> {
    return string { it?.toDurationOrNull() }
}

fun <T> Enver.duration(block: (Duration?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toDurationOrNull()) }
}

fun Enver.duration(name: String): EnverProperty<Duration?> {
    return string(name) { it?.toDurationOrNull() }
}

fun <T> Enver.duration(name: String, block: (Duration?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toDurationOrNull()) }
}

//
