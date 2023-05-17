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
package net.lsafer.enver.internal

import net.lsafer.enver.InternalEnverApi
import java.util.logging.Logger

private val logger = Logger.getLogger("net.lsafer.enver")

// (name: String, value: String?, error: String) -> Boolean
internal typealias ErrorHandler = (Triple<String, String?, String>) -> Boolean

private val envNameRegex = Regex("[\\w.-]+")
private val envValueRegex = Regex("([\\w.-]*)|(['\"].*['\"])")
private val envQueuesRegex = Regex("^['\"].*['\"]$")

/**
 * Parse the given [content] and using [onError]
 * handler when an error occurs.
 */
@InternalEnverApi
fun parse(content: String, onError: ErrorHandler): Map<String, String> {
    return content.split("\n")
        .asSequence()
        .map { line -> line.trim() }
        .filterNot { line ->
            line.isBlank() ||
            line.startsWith("#") ||
            line.startsWith("////")
        }
        .map { line ->
            val segments = line
                .split("=", limit = 2)
                .map { it.trim() }

            segments[0] to segments.getOrNull(1)?.let {
                when {
                    it matches envQueuesRegex ->
                        it.substring(1, it.length - 1)
                    else -> it
                }
            }
        }
        .map { (name, value) ->
            when {
                !name.matches(envNameRegex) ->
                    Triple(name, value, "Illegal environment variable name: $name")
                value == null ->
                    Triple(name, "", "Missing `=` after environment variable: $name")
                !value.matches(envValueRegex) ->
                    Triple(name, value, "Illegal environment variable value: $name=$value")
                else ->
                    Triple(name, value, null)
            }
        }
        .filter { triple ->
            triple.third ?: return@filter true
            @Suppress("UNCHECKED_CAST")
            onError(triple as Triple<String, String?, String>)
        }
        .associate { (name, value) ->
            name to (value ?: "")
        }
}

private val lookupRegex = Regex("[$][{]([^}{$]*)[}]")

@InternalEnverApi
fun parseLookups(sources: List<Map<String, String>>): Map<String, String> {
    val histograms = sources.histogram()

    fun lookup(name: String, dejaVu: List<String> = emptyList(), depth: Int = 0): String {
        val value = histograms[name]?.getOrNull(depth) ?: return ""

        return lookupRegex.replace(value) {
            when (val target = it.groupValues[1]) {
                name -> lookup(target, dejaVu, depth + 1)
                in dejaVu -> {
                    logger.warning("Recursive environment lookup: $name")
                    ""
                }
                else -> lookup(target, dejaVu + name)
            }
        }
    }

    return histograms.keys.associateWith { lookup(it) }
}

private fun List<Map<String, String>>.histogram(): Map<String, List<String>> {
    val o = mutableMapOf<String, MutableList<String>>()
    forEach { it.forEach { (k, v) -> o.getOrPut(k) { mutableListOf() }.add(0, v) } }
    return o
}
