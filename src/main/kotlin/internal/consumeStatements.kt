package net.lsafer.enver.internal

import net.lsafer.enver.EnverDotenvOptions

internal interface StatementConsumer {
    fun onStatement(lineNumber: Int, line: String, name: String, value: String)

    fun onMissingAssignmentOperator(lineNumber: Int, line: String, name: String)

    fun onMissingClosingQuote(lineNumber: Int, line: String, name: String, value: String)
}

internal fun Sequence<String>.consumeStatements(consumer: StatementConsumer) {
    val iterator = withIndex().iterator()

    while (iterator.hasNext()) {
        val (lineNumber, line) = iterator.next()

        // handle blank lines
        if (line.isBlank()) continue

        val segments = line.split('=', limit = 2)
        val name = segments[0].trim()
        // the value segment in the first line
        val value0 = segments.getOrNull(1)

        // handle whole line comments
        if (name[0] == '#') continue

        // handle lines with no `=` sign
        if (value0 == null) {
            consumer.onMissingAssignmentOperator(lineNumber, line, name)
            continue
        }

        // handle blank values
        if (value0.isBlank()) {
            consumer.onStatement(lineNumber, line, name, "")
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

            // escaped on `StringBuilder.append` and not
            // on `StringBuilder.toString` to not treat
            // escape characters at the end of the lines as an
            // escape to the characters at the
            // beginning of the next lines
            while (true) {
                val terminal = valueN.indexOfNotEscapedByBackslash(quote)

                if (terminal < 0) {
                    // the literal has not ended in this line
                    // take the next line as value continuation
                    value.append(valueN.unescapeBackslashEscapes(quote))

                    // reached EOF without closing the literal
                    if (!iterator.hasNext()) {
                        consumer.onMissingClosingQuote(lineNumber, line, name, value.toString())
                        break
                    }

                    value.appendLine()
                    valueN = iterator.next().value
                } else {
                    // the literal has ended in this line
                    // treat the remaining as comments
                    value.append(valueN.take(terminal).unescapeBackslashEscapes(quote))
                    consumer.onStatement(lineNumber, line, name, value.toString())
                    break
                }
            }

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
        for (i in 0..<terminal) {
            if (!value0[i].isWhitespace()) {
                offset = i
                break
            }
        }

        consumer.onStatement(lineNumber, line, name, value0.substring(offset, terminal))
    }
}

internal fun createStatementConsumer(
    output: MutableMap<String, String>,
    filename: String?,
    options: EnverDotenvOptions,
): StatementConsumer {
    return object : StatementConsumer {
        override fun onStatement(lineNumber: Int, line: String, name: String, value: String) {
            output[name] = value
        }

        override fun onMissingAssignmentOperator(lineNumber: Int, line: String, name: String) =
            onIncompleteStatement(lineNumber, line, name, "")

        override fun onMissingClosingQuote(lineNumber: Int, line: String, name: String, value: String) =
            onIncompleteStatement(lineNumber, line, name, value)

        fun onIncompleteStatement(lineNumber: Int, line: String, name: String, value: String) {
            val cause = IllegalArgumentException(
                "Incomplete environment statement $name from ${filename ?: "unknown"} at $lineNumber:\n$line"
            )

            if (options.onIncompleteStatement(cause)) {
                output[name] = value
            }
        }
    }
}
