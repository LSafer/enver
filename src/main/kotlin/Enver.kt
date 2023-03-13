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

import net.lsafer.enver.internal.EnverImpl

/**
 * A reactive instance holding environment manually
 * loaded variables.
 *
 * Every instance of this class is initialized
 * with nothing and the user is responsible for
 * populating it using either [set] or [plusAssign].
 *
 * @author LSafer
 * @since 1.0.0
 */
interface Enver {
    companion object : Enver by Enver()

    /**
     * Instantly get the current environment
     * variable with the given [name].
     *
     * @since 1.0.0
     */
    operator fun get(name: String): String?

    /**
     * Set the environment variable with the given
     * [name] to the given [value].
     *
     * If the given [value] is null, the
     * environment variable is removed.
     *
     * @since 1.0.0
     */
    operator fun set(name: String, value: String?)

    /**
     * Set the environment variables in the given [source].
     */
    operator fun plusAssign(source: Map<String, String>)

    /**
     * Add the given [block] to be invoked on
     * every change.
     *
     * The block will be invoked immediately with
     * the current values.
     *
     * @param key the key to be used to unsubscribe with. (default to [block])
     */
    @ExperimentalEnverApi
    fun subscribe(block: (Map<String, String>) -> Unit, key: Any)

    /**
     * Remove the blocks subscribed using the given
     * [key] from being invoked on every change.
     */
    @ExperimentalEnverApi
    fun unsubscribe(key: Any)
}

/**
 * Add the given [block] to be invoked on
 * every change.
 */
@ExperimentalEnverApi
fun Enver.subscribe(block: (Map<String, String>) -> Unit) {
    subscribe(block, block)
}

/**
 * Add the given [block] to be invoked on every
 * change for the variable with the given [name].
 */
@ExperimentalEnverApi
fun Enver.subscribe(name: String, block: (String) -> Unit) {
    subscribe(name, block, block)
}

/**
 * Add the given [block] to be invoked on every
 * change for the variable with the given [name].
 *
 * @param key the key to be used to unsubscribe with. (default to [block])
 */
@ExperimentalEnverApi
fun Enver.subscribe(name: String, block: (String) -> Unit, key: Any) {
    subscribe({ it[name]?.let(block) }, key)
}

/**
 * Create a new [Enver] instance.
 */
@OptIn(InternalEnverApi::class)
fun Enver(): Enver {
    return EnverImpl()
}
