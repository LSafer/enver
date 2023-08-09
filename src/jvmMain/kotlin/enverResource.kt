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

import net.lsafer.enver.internal.consumeStatements
import net.lsafer.enver.internal.createStatementConsumer

/**
 * The configuration needed to obtain an
 * environment variables table from a file in the
 * resources bundled with the application.
 */
open class EnverResourceOptions : EnverDotenvOptions() {
    /**
     * The name of the resource.
     */
    var filename: String = ""

    /**
     * True, to throw if the resource is missing.
     */
    @Deprecated("use onNotFound instead")
    var required: Boolean = false

    var onNotFound = { e: Throwable -> e.printStackTrace() }
}

/**
 * Obtain an environment variables table from the
 * resource with the filename provided in the given
 * configuration [block].
 */
fun enverResource(
    filename: String = "",
    block: EnverResourceOptions.() -> Unit = {},
): Map<String, String> {
    val configuration = EnverResourceOptions()
    configuration.filename = filename
    configuration.apply(block)
    return enverResource(configuration)
}

/**
 * Obtain an environment variables table from the
 * resource with the filename provided in the given
 * [options].
 *
 * @param options the configuration object.
 */
fun enverResource(options: EnverResourceOptions): Map<String, String> {
    val stream = classLoader().getResourceAsStream(options.filename)

    if (stream == null) {
        val cause = IllegalArgumentException("Resource not found: ${options.filename}")

        @Suppress("Deprecation")
        if (options.required)
            throw cause

        options.onNotFound(cause)
        return emptyMap()
    }

    val output = mutableMapOf<String, String>()
    val consumer = createStatementConsumer(output, options.filename, options)
    stream.bufferedReader().use { it.lineSequence().consumeStatements(consumer) }
    return output
}

private fun classLoader(): ClassLoader {
    return Thread.currentThread().contextClassLoader
}
