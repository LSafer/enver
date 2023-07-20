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

import java.lang.StringBuilder
import java.util.logging.Logger

private val logger = Logger.getLogger("net.lsafer.enver")

// (name: String, value: String?, error: String) -> Boolean
internal typealias ErrorHandler = (Triple<String, String?, String>) -> Boolean

/**
 * Parse the given [content] and using [onError]
 * handler when an error occurs.
 */
internal fun parse(content: String, onError: ErrorHandler): Map<String, String> {
    val map = mutableMapOf<String, String>()
    val iterator = content.lines().iterator()

    while (iterator.hasNext()) {
        val line = iterator.next()

        // handle blank lines
        if (line.isBlank()) continue

        val segments = line.split('=', limit = 2)
        val name = segments[0].trim()
        // the value segment in the first line
        val value0 = segments.getOrNull(1)

        // handle whole line comments
        if (name[0] == '#') continue

        // handle blank values and lines with no '=' sign
        if (value0.isNullOrBlank()) {
            // TODO report error when value is null
            map[name] = ""
            continue
        }

        var quote = '\''
        var quoteIndex = -1

        // find the first quote if any
        for (i in value0.indices) {
            when (val c = value0[i]) {
                '\'', '\"' -> {
                    quote = c
                    quoteIndex = i
                }
                else -> {
                    if (!c.isWhitespace())
                        break
                }
            }
        }

        // if the value starts with a `quote`, the value could be multiline,
        // everything before the second unescaped `quote` is within the content,
        // everything after the second unescaped `quote` is a comment
        // + the unescaped `quote` should not be included in the value.
        if (quoteIndex >= 0) {
            val value = StringBuilder()
            var valueN = value0.drop(quoteIndex + 1)

            while (true) {
                val terminal = valueN.indexOfUnescaped(quote, '\\')

                if (terminal < 0) {
                    // the literal has not ended in this line
                    // take the next line as value continuation
                    value.append(valueN.unescape(quote, '\\'))

                    // reached EOF without closing the literal
                    if (!iterator.hasNext()) {
                        // TODO report error
                        break
                    }

                    value.appendLine()
                    valueN = iterator.next()
                } else {
                    // the literal has ended in this line
                    // treat the remaining as comments
                    value.append(valueN.take(terminal).unescape(quote, '\\'))
                    break
                }
            }

            // escaped before appending and not
            // here to not treat escape characters
            // at the end of the lines as an
            // escape to the characters at the
            // beginning of the next lines
            map[name] = value.toString()

            continue
        }

        // if the value does not start with `quote`,
        // everything after the first `#` is a comment
        // + the `#` should not be included in the value.

        var terminal = value0.indexOf('#')
        var offset = 0

        if (terminal == -1) {
            terminal = value0.length
        }

        // shortcut for trimming instead of creating a new string
        for (i in terminal - 1 downTo 0) {
            if (!value0[i].isWhitespace()) {
                terminal = i + 1
                break
            }
        }
        for (i in 0 until terminal) {
            if (!value0[i].isWhitespace()) {
                offset = i
                break
            }
        }

        map[name] = value0.substring(offset, terminal)
    }

    return map
}

private val lookupRegex = Regex("[$][{]([^}{$]*)[}]")

internal fun parseLookups(sources: List<Map<String, String>>): Map<String, String> {
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

/** find the index of the next unescaped [char] by an unescaped [escape] */
private fun String.indexOfUnescaped(char: Char, escape: Char, startIndex: Int = 0): Int {
    var cursor = startIndex

    while (true) {
        cursor = indexOf(char, cursor)

        if (cursor < 0) return -1

        val escapeCount = run {
            for (i in cursor - 1 downTo 0) {
                if (get(i) != escape)
                    return@run cursor - i - 1
            }

            cursor
        }

        // if leaded by an even number of escapes
        // the escapes escape each other
        if (escapeCount % 2 == 0)
            return cursor

        cursor++
    }
}

/** unescape all escaped [char] or [escape] */
private fun String.unescape(char: Char, escape: Char): String {
    /*
    The approach is to first collect all the
    positions of the unescaped escape characters
    and then build a string that does not include
    them.
    */

    val foundEscapes = mutableListOf<Int>()
    var isEscaping = false

    for (i in indices) {
        val c = get(i)

        if (isEscaping && (c == char || c == escape)) {
            foundEscapes += i - 1
            isEscaping = false
            continue
        }

        if (c == escape) {
            isEscaping = true
            continue
        }
    }

    if (foundEscapes.isEmpty())
        return this

    val out = StringBuilder()

    val iterator = foundEscapes.iterator()

    var lastEscape = iterator.next()

    out.append(this, 0, lastEscape)

    while (iterator.hasNext()) {
        val nextEscape = iterator.next()

        out.append(this, lastEscape + 1, nextEscape)

        lastEscape = nextEscape
    }

    out.append(this, lastEscape + 1, length)

    return out.toString()
}
