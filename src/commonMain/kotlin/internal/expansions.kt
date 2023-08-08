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

internal fun mergeWithExpansions(sources: List<Map<String, String>>): Map<String, String> {
    val entries = sources.groupByName()
    return entries.keys.associateWith { entries.lookup(it) }
}

private fun List<Map<String, String>>.groupByName(): Map<String, List<String>> {
    val output = mutableMapOf<String, MutableList<String>>()
    forEach {
        it.forEach { (name, value) ->
            val stack = output.getOrPut(name) { mutableListOf() }

            stack.add(0, value)
        }
    }
    return output
}

private fun Map<String, List<String>>.lookup(name: String, dejaVu: List<String> = emptyList(), depth: Int = 0): String {
    val value = this[name]?.getOrNull(depth) ?: return ""

    val lookups = generateEnclosureSequenceSinglePointer(
        openOffsets = value.indexOfNotEscapedByBackslashSequence("\${"),
        closeOffsets = value.indexOfNotEscapedByBackslashSequence("}")
    ).map { it to value.substring(it.first + 2, it.last - 1).trim() }

    val output = StringBuilder()

    var prev = 0
    for ((range, target) in lookups) {
        val replacement = when (target) {
            name -> {
                lookup(target, dejaVu, depth + 1)
            }

            in dejaVu -> {
                println("WARNING: recursive environment lookup: '$target' at '$name' was replaced with an empty string")
                ""
            }

            else -> {
                lookup(target, dejaVu + name)
            }
        }

        output.append(value, prev, range.first)
        output.append(replacement)
        prev = range.last
    }

    output.append(value, prev, value.length)

    return output.toString()
}
