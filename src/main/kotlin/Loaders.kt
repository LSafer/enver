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

import net.lsafer.enver.internal.ErrorHandler
import net.lsafer.enver.internal.parse
import org.intellij.lang.annotations.Language
import java.io.File

/**
 * The configuration needed to obtain an
 * environment variables table from the system.
 */
class EnverSystemConfiguration {
    /**
     * Set to true to disable security checks.
     */
    var ignoreSecurityCheck: Boolean = true
}

/**
 * Return a table containing the environment
 * variables of the system. (using [System.getenv])
 *
 * @param block a block building the configuration object.
 */
fun enverSystem(
    block: EnverSystemConfiguration.() -> Unit = {}
): Map<String, String> {
    val configuration = EnverSystemConfiguration()
    configuration.apply(block)
    return enverSystem(configuration)
}

/**
 * Return a table containing the environment
 * variables of the system. (using [System.getenv])
 *
 * @param configuration the configuration object.
 */
fun enverSystem(
    configuration: EnverSystemConfiguration
): Map<String, String> {
    return try {
        System.getenv()
    } catch (error: SecurityException) {
        if (!configuration.ignoreSecurityCheck)
            throw error

        error.printStackTrace()
        emptyMap()
    }
}

// EnverFile

/**
 * The configuration needed to obtain an
 * environment variables table from a file.
 */
class EnverFileConfiguration {
    /**
     * The environment file.
     */
    var file: File = File(".env")

    /**
     * True, to throw if the file is missing.
     */
    var required: Boolean = false

    /**
     * The parsing error handler.
     */
    var onError: ErrorHandler = { true }
}

/**
 * Obtain an environment variables table from the
 * file in the given configuration [block].
 *
 * @param file the initial file in the configuration object.
 * @param block a block building the configuration object.
 */
fun enverFile(
    file: String,
    block: EnverFileConfiguration.() -> Unit = {}
): Map<String, String> {
    return enverFile(File(file), block)
}

/**
 * Obtain an environment variables table from the
 * file in the given configuration [block].
 *
 * @param file the initial file in the configuration object.
 * @param block a block building the configuration object.
 */
fun enverFile(
    file: File = File(".env"),
    block: EnverFileConfiguration.() -> Unit = {}
): Map<String, String> {
    val configuration = EnverFileConfiguration()
    configuration.file = file
    configuration.apply(block)
    return enverFile(configuration)
}

/**
 * Obtain an environment variables table from the
 * file in the given [configuration].
 *
 * @param configuration the configuration object.
 */
@OptIn(InternalEnverApi::class)
fun enverFile(configuration: EnverFileConfiguration): Map<String, String> {
    val content = configuration.file
        .takeIf { it.isFile }
        ?.readText()

    if (content == null) {
        if (configuration.required)
            error("File Not Found: ${configuration.file}")

        return emptyMap()
    }

    return parse(content, configuration.onError)
}

// EnverResource

/**
 * The configuration needed to obtain an
 * environment variables table from a file in the
 * resources bundled with the application.
 */
class EnverResourceConfiguration {
    /**
     * The name of the resource.
     */
    var filename: String = ".env"

    /**
     * True, to throw if the resource is missing.
     */
    var required: Boolean = false

    /**
     * The parsing error handler.
     */
    var onError: ErrorHandler = { true }
}

/**
 * Obtain an environment variables table from the
 * resource with the filename provided in the given
 * configuration [block].
 */
fun enverResource(
    filename: String = "",
    block: EnverResourceConfiguration.() -> Unit
): Map<String, String> {
    val configuration = EnverResourceConfiguration()
    configuration.filename = filename
    configuration.apply(block)
    return enverResource(configuration)
}

/**
 * Obtain an environment variables table from the
 * resource with the filename provided in the given
 * [configuration].
 *
 * @param configuration the configuration object.
 */
@OptIn(InternalEnverApi::class)
fun enverResource(configuration: EnverResourceConfiguration): Map<String, String> {
    val content = Thread
        .currentThread()
        .contextClassLoader
        .getResourceAsStream(configuration.filename)
        ?.bufferedReader()
        ?.readText()

    if (content == null) {
        if (configuration.required)
            error("Resource Not Found: ${configuration.filename}")

        return emptyMap()
    }

    return parse(content, configuration.onError)
}

// EnverSource

/**
 * The configuration needed to obtain an
 * environment variables table from a raw source.
 */
class EnverSourceConfiguration {
    /**
     * The raw source.
     */
    @Language("dotenv")
    var source: String = ""

    /**
     * The parsing error handler.
     */
    var onError: ErrorHandler = { true }
}

/**
 * Obtain an environment variables table from the
 * source in the given configuration [block].
 */
fun enverSource(
    @Language("dotenv")
    source: String = "",
    block: EnverSourceConfiguration.() -> Unit
): Map<String, String> {
    val configuration = EnverSourceConfiguration()
    configuration.source = source
    configuration.apply(block)
    return enverSource(configuration)
}

/**
 * Obtain an environment variables table from the
 * source in the given [configuration].
 *
 * @param configuration the configuration object.
 */
@OptIn(InternalEnverApi::class)
fun enverSource(configuration: EnverSourceConfiguration): Map<String, String> {
    return parse(configuration.source, configuration.onError)
}
