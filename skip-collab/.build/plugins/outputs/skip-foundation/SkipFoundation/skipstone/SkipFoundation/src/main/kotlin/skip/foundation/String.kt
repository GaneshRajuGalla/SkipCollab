// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

typealias NSString = kotlin.String
fun NSString(string: String): kotlin.String = string

fun strlen(string: String): Int = string.count

fun strncmp(str1: String, str2: String): Int = if (str1.toLowerCase() == str2.toLowerCase()) 0 else 1

val String.capitalized: String
    get() {
        return split(separator = ' ', omittingEmptySubsequences = false)
            .joinToString(separator = " ") { it ->
                it.replaceFirstChar { it -> it.titlecase() }
            }
    }

val String.deletingLastPathComponent: String
    get() {
        val lastSeparatorIndex = lastIndexOf("/")
        if (lastSeparatorIndex == -1 || (lastSeparatorIndex == 0 && this.length == 1)) {
            return this
        }
        val newPath = substring(0, lastSeparatorIndex)
        val newLastSeparatorIndex = newPath.lastIndexOf("/")
        if (newLastSeparatorIndex == -1) {
            return newPath
        } else {
            return newPath.substring(0, newLastSeparatorIndex + 1)
        }
    }

fun String.replacingOccurrences(of: String, with: String): String {
    val search = of
    val replacement = with
    return replace(search, replacement)
}

fun String.components(separatedBy: String): Array<String> {
    val separator = separatedBy
    return Array(split(separator, ignoreCase = false))
}

fun String.trimmingCharacters(in_: CharacterSet): String {
    val set = in_
    return trim { it -> set.platformValue.contains(it) }
}
