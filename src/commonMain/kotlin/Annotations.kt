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

/**
 * Marks the annotated component as experimental.
 *
 * @since 1.0.0
 */
@RequiresOptIn(
    message = "This is an experimental API and might change at anytime",
    level = RequiresOptIn.Level.WARNING
)
annotation class ExperimentalEnverApi(
    /**
     * Optionally, the reason why the component was marked with this annotation.
     */
    val reason: String = "This is an experimental API and might change at anytime"
)
