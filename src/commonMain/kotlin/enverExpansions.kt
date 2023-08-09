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

import net.lsafer.enver.internal.mergeWithExpansions

/**
 * Take the given [sources] and perform variable
 * expansions on them and return the result.
 *
 * Long recursive variable expansions will be replaced
 * with an empty string and display a logger warning.
 *
 * ```dotenv
 * # .env
 * A = "A<${B}>"
 * B = "B<${A}>"
 *
 * # `A` will output: `A<B<>>`
 * # `B` will output: `B<A<>>`
 * ```
 *
 * Variables that has a lookup to itself will be
 * replaced with the previous variable passed to it.
 *
 * ```dotenv
 * # foo.env
 * A = "1,${A}"
 *
 * # bar.env
 * A = "2,${A}"
 *
 * # if `foo` is before `bar`, `A` will output `2,1,`
 * # if `bar` is before `foo`, `A` will output `1,2,`
 * ```
 */
fun enverExpansions(sources: List<Map<String, String>>): Map<String, String> {
    return mergeWithExpansions(sources)
}

/**
 * Take the given [sources] and perform variable
 * expansions on them and return the result.
 */
fun enverExpansions(vararg sources: Map<String, String>): Map<String, String> {
    return enverExpansions(sources.asList())
}
