package net.lsafer.enver.internal

internal fun String.indexOfNotEscapedByBackslash(char: Char, startIndex: Int = 0): Int {
    var cursor = startIndex

    while (true) {
        cursor = indexOf(char, cursor)

        if (cursor < 0) return -1

        val slashCount = countConsecutiveLeft('\\', cursor - 1)

        if (slashCount % 2 == 0)
            return cursor

        cursor++
    }
}

internal fun String.indexOfNotEscapedByBackslashSequence(string: String, startIndex: Int = 0): Sequence<Int> {
    return sequence {
        var cursor = startIndex

        while (true) {
            cursor = indexOf(string, cursor)

            if (cursor < 0) return@sequence

            val slashCount = countConsecutiveLeft('\\', cursor - 1)

            if (slashCount % 2 == 0)
                yield(cursor)

            cursor++
        }
    }
}

internal fun String.countConsecutiveLeft(char: Char, fromInclusive: Int): Int {
    for (i in fromInclusive downTo 0) {
        if (this[i] != char) {
            return fromInclusive - i
        }
    }

    return fromInclusive + 1
}

internal fun String.unescapeBackslashEscapes(vararg chars: Char): String {
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

        if (isEscaping && (c == '\\' || c in chars)) {
            foundEscapes += i - 1
            isEscaping = false
            continue
        }

        if (c == '\\') {
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
        val currentEscape = iterator.next()

        out.append(this, lastEscape + 1, currentEscape)

        lastEscape = currentEscape
    }

    out.append(this, lastEscape + 1, length)

    return out.toString()
}

///////////////////////////////////

internal fun generateEnclosureSequenceDoublePointer(
    openOffsets: Sequence<Int>, // SORTED
    closeOffsets: Sequence<Int>, // SORTED
): Sequence<IntRange> {
    val openList = openOffsets.toMutableList()
    var openIterator = openList.listIterator(openList.size)
    val closeIterator = closeOffsets.iterator()

//        { { { } } } { { { } } }
//            | ] |   | | [
//            | ] |   | [ |
//            | ] |   [   |
//            [ ] |       |
//                ]       [
    return sequence {
        var openOffset = openIterator.previousOrNull() ?: return@sequence
        var closeOffset = closeIterator.nextOrNull() ?: return@sequence

        while (true) {
            if (openOffset > closeOffset) {
                openOffset = openIterator.previousOrNull() ?: return@sequence
                continue
            }

            yield(openOffset..<closeOffset)

            closeOffset = closeIterator.nextOrNull() ?: return@sequence

            openIterator.remove()
            openIterator = openList.listIterator(openList.size)
            openOffset = openIterator.previous()
        }
    }
}

internal fun generateEnclosureSequenceSinglePointer(
    openOffsets: Sequence<Int>, // SORTED
    closeOffsets: Sequence<Int>, // SORTED
): Sequence<IntRange> {
    val lifo = mutableListOf<Int>()
    val offsets = sequenceOf(openOffsets, closeOffsets).flattenSorted()
    return sequence {
        for ((offset, type) in offsets) {
            when (type) {
                0 -> { // OPEN
                    lifo += offset
                }

                1 -> { // CLOSE
                    val open = lifo.removeLast()

                    yield(open..<offset)
                }
            }
        }
    }
}

///////////////////////////////////

private fun <T : Comparable<T>> Sequence<Sequence<T>>.flattenSorted(): Sequence<Pair<T, Int>> {
    return sequence {
        val candidates = mutableListOf<Triple<Int, Iterator<T>, T>>()

        forEachIndexed { origin, sequence ->
            val iterator = sequence.iterator()

            if (iterator.hasNext()) {
                candidates += Triple(origin, iterator, iterator.next())
            }
        }

        while (true) {
            val (i, tuple) = candidates.withIndex().minByOrNull { (_, it) -> it.third } ?: break
            val (origin, iterator, value) = tuple

            yield(value to origin)

            if (iterator.hasNext()) {
                candidates[i] = Triple(origin, iterator, iterator.next())
            } else {
                candidates.removeAt(i)
            }
        }
    }
}

private fun <T : Any> ListIterator<T>.previousOrNull(): T? =
    if (hasPrevious()) previous() else null

private fun <T : Any> Iterator<T>.nextOrNull(): T? =
    if (hasNext()) next() else null
