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
import org.intellij.lang.annotations.Language

open class EnverDotenvOptions {
    var onIncompleteStatement = { e: Throwable -> e.printStackTrace(); false }
}

/**
 * The configuration needed to obtain an
 * environment variables table from a raw source.
 */
class EnverSourceOptions : EnverDotenvOptions() {
    /**
     * The raw source.
     */
    @Language("dotenv")
    var source: String = ""
}

/**
 * Obtain an environment variables table from the
 * source in the given configuration [block].
 */
fun enverSource(
    @Language("dotenv")
    source: String = "",
    block: EnverSourceOptions.() -> Unit = {},
): Map<String, String> {
    val configuration = EnverSourceOptions()
    configuration.source = source
    configuration.apply(block)
    return enverSource(configuration)
}

/**
 * Obtain an environment variables table from the
 * source in the given [options].
 *
 * @param options the configuration object.
 */
fun enverSource(options: EnverSourceOptions): Map<String, String> {
    val source = options.source

    val output = mutableMapOf<String, String>()
    val consumer = createStatementConsumer(output, null, options)
    source.lineSequence().consumeStatements(consumer)
    return output
}
