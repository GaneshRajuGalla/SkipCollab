// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

internal typealias PlatformStringEncoding = StringEncoding

fun String(bytes: Array<UByte>, encoding: StringEncoding): String? {
    val byteArray = ByteArray(size = bytes.count) l@{ it -> return@l bytes[it].toByte() }
    return byteArray.toString(encoding.rawValue)
}

fun String.data(using: StringEncoding, allowLossyConversion: Boolean = true): Data? {
    return try { Data(platformValue = toByteArray(using.rawValue)) } catch (_: Throwable) { null }
}

val String.utf8: Array<UByte>
    get() {
        // TODO: there should be a faster way to convert a string to a UInt8 array
        return Array(toByteArray(StringEncoding.utf8.rawValue).map { it.toUByte() })
    }

val String.utf16: Array<UByte>
    get() {
        return Array(toByteArray(StringEncoding.utf16.rawValue).map { it.toUByte() })
    }

val String.unicodeScalars: Array<UByte>
    get() {
        return Array(toByteArray(StringEncoding.utf8.rawValue).map { it.toUByte() })
    }

class StringEncoding: RawRepresentable<java.nio.charset.Charset> {

    override val rawValue: java.nio.charset.Charset

    constructor(rawValue: java.nio.charset.Charset) {
        this.rawValue = rawValue.sref()
    }

    constructor(rawValue: java.nio.charset.Charset, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.rawValue = rawValue.sref()
    }

    val description: String
        get() = rawValue.description

    override fun equals(other: Any?): Boolean {
        if (other !is StringEncoding) return false
        return rawValue == other.rawValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, rawValue)
        return result
    }

    companion object {
        val utf8 = StringEncoding(rawValue = Charsets.UTF_8)
        val utf16 = StringEncoding(rawValue = Charsets.UTF_16)
        val utf16LittleEndian = StringEncoding(rawValue = Charsets.UTF_16LE)
        val utf16BigEndian = StringEncoding(rawValue = Charsets.UTF_16BE)
        val utf32 = StringEncoding(rawValue = Charsets.UTF_32)
        val utf32LittleEndian = StringEncoding(rawValue = Charsets.UTF_32LE)
        val utf32BigEndian = StringEncoding(rawValue = Charsets.UTF_32BE)
    }
}
