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

/**
 * The configuration needed to obtain an
 * environment variables table from the system.
 */
open class EnverSystemOptions {
    /**
     * Set to true to disable security checks.
     */
    @Deprecated("use onSecurityError instead")
    var ignoreSecurityCheck: Boolean = true

    /**
     * What to do when a security error occurs while
     * attempting to read system environment variables
     */
    var onSecurityError = { e: Throwable -> e.printStackTrace() }
}

/**
 * Return a table containing the environment
 * variables of the system. (using [System.getenv])
 *
 * @param block a block building the configuration object.
 */
fun enverSystem(
    block: EnverSystemOptions.() -> Unit = {},
): Map<String, String> {
    val options = EnverSystemOptions()
    options.apply(block)
    return enverSystem(options)
}

/**
 * Return a table containing the environment
 * variables of the system. (using [System.getenv])
 *
 * @param options the configuration object.
 */
fun enverSystem(options: EnverSystemOptions): Map<String, String> {
    return try {
        System.getenv()
    } catch (error: SecurityException) {
        val cause = IllegalStateException("Cannot read environment variables due to security checks", error)

        @Suppress("Deprecation")
        if (!options.ignoreSecurityCheck)
            throw cause

        options.onSecurityError(cause)
        return emptyMap()
    }
}
