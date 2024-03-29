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

import java.io.File

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toFileOrNull(): File? {
    return File(this).takeIf { it.isFile }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified file and non existing files cannot be achieved")
fun Enver.file(): EnverPropertyProvider<File?> {
    return string { it?.toFileOrNull() }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified file and non existing files cannot be achieved")
fun <T> Enver.file(block: (File?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toFileOrNull()) }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified file and non existing files cannot be achieved")
fun Enver.file(name: String): EnverProperty<File?> {
    return string(name) { it?.toFileOrNull() }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified file and non existing files cannot be achieved")
fun <T> Enver.file(name: String, block: (File?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toFileOrNull()) }
}

//

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toDirectoryOrNull(): File? {
    return File(this).takeIf { it.isDirectory }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified directories and non existing directories cannot be achieved")
fun Enver.directory(): EnverPropertyProvider<File?> {
    return string { it?.toDirectoryOrNull() }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified directories and non existing directories cannot be achieved")
fun <T> Enver.directory(block: (File?) -> T): EnverPropertyProvider<T> {
    return string { block(it?.toDirectoryOrNull()) }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified directories and non existing directories cannot be achieved")
fun Enver.directory(name: String): EnverProperty<File?> {
    return string(name) { it?.toDirectoryOrNull() }
}

@Deprecated("use Enver.string instead. Distinguishing unspecified directories and non existing directories cannot be achieved")
fun <T> Enver.directory(name: String, block: (File?) -> T): EnverProperty<T> {
    return string(name) { block(it?.toDirectoryOrNull()) }
}

//
