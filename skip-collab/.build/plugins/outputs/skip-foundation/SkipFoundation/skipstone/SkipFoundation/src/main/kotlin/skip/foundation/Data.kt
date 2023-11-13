// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformData = kotlin.ByteArray
internal typealias PlatformDataProtocol = kotlin.ByteArray
typealias NSData = Data

interface DataProtocol {
    val platformData: kotlin.ByteArray
}

class Data: DataProtocol, Codable, MutableStruct {
    internal var platformValue: kotlin.ByteArray
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    override val platformData: kotlin.ByteArray
        get() = platformValue

    constructor(platformValue: kotlin.ByteArray) {
        this.platformValue = platformValue
    }

    constructor(data: Data) {
        this.platformValue = data.platformValue
    }

    constructor(bytes: Array<UByte>, length: Int? = null) {
        this.platformValue = kotlin.ByteArray(size = length ?: bytes.count, init = { it -> bytes[it].toByte() })
    }


    constructor(base64Encoded: String) {
        val data_0 = try { java.util.Base64.getDecoder().decode(base64Encoded) } catch (_: Throwable) { null }
        if (data_0 == null) {
            throw NullReturnException()
        }
        this.platformValue = data_0
    }

    constructor(from: Decoder) {
        val decoder = from
        var container = decoder.unkeyedContainer()
        var bytes: Array<UByte> = arrayOf()
        while (!container.isAtEnd) {
            bytes.append(container.decode(UByte::class))
        }
        this.platformValue = kotlin.ByteArray(size = bytes.count, init = { it -> bytes[it].toByte() })
    }

    override fun encode(to: Encoder) {
        val encoder = to
        var container = encoder.unkeyedContainer()
        for (b in this.bytes.sref()) {
            container.encode(b)
        }
    }

    val count: Int
        get() = platformValue.size

    val bytes: Array<UByte>
        get() {
            return Array(platformValue.map { it -> it.toUByte() })
        }

    // Platform declaration clash: The following declarations have the same JVM signature (<init>(Lskip/lib/Array;)V):
    //public init(_ bytes: [Int]) {
    //    self.platformValue = PlatformData(size: bytes.count, init: {
    //        bytes[$0].toByte()
    //    })
    //}

    val description: String
        get() = platformValue.description

    val utf8String: String?
        get() = String(data = this, encoding = StringEncoding.utf8)

    constructor() {
        this.platformValue = kotlin.ByteArray(size = 0)
    }

    constructor(count: Int) {
        this.platformValue = kotlin.ByteArray(size = count)
    }

    constructor(capacity: Int, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        // No equivalent kotlin.ByteArray(capacity:), so allocate with zero
        this.platformValue = kotlin.ByteArray(size = 0)
    }

    fun append(contentsOf: Array<UByte>) {
        val bytes = contentsOf
        willmutate()
        try {
            this.platformValue += Data(bytes).platformValue
        } finally {
            didmutate()
        }
    }

    fun append(contentsOf: Data) {
        val data = contentsOf
        willmutate()
        try {
            this.platformValue += data.platformValue
        } finally {
            didmutate()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Data) {
            return false
        }
        val lhs = this
        val rhs = other
        return lhs.platformValue.contentEquals(rhs.platformValue)
    }

    fun write(to: URL, options: Data.WritingOptions = Data.WritingOptions.of()) {
        val url = to
        var opts: Array<java.nio.file.StandardOpenOption> = arrayOf()
        opts.append(java.nio.file.StandardOpenOption.CREATE)
        opts.append(java.nio.file.StandardOpenOption.WRITE)
        if (options.contains(Data.WritingOptions.atomic)) {
            opts.append(java.nio.file.StandardOpenOption.DSYNC)
        }

        java.nio.file.Files.write(url.toPath(), platformValue, *(opts.toList().toTypedArray()))
    }

    class WritingOptions: OptionSet<Data.WritingOptions, UInt>, Sendable {
        override var rawValue: UInt
        constructor(rawValue: UInt) {
            this.rawValue = rawValue
        }

        override val rawvaluelong: ULong
            get() = ULong(rawValue)
        override fun makeoptionset(rawvaluelong: ULong): Data.WritingOptions = WritingOptions(rawValue = UInt(rawvaluelong))
        override fun assignoptionset(target: Data.WritingOptions): Unit = assignfrom(target)

        private fun assignfrom(target: Data.WritingOptions) {
            this.rawValue = target.rawValue
        }

        companion object {

            val atomic = WritingOptions(rawValue = UInt(1 shl 0))

            fun of(vararg options: Data.WritingOptions): Data.WritingOptions {
                val value = options.fold(UInt(0)) { result, option -> result or option.rawValue }
                return WritingOptions(rawValue = value)
            }
        }
    }

    fun kotlin(nocopy: Boolean = false): kotlin.ByteArray = if (nocopy) platformValue else platformValue.copyOf()

    fun base64EncodedString(): String = java.util.Base64.getEncoder().encodeToString(platformValue)

    fun sha256(): Data = Data(SHA256.hash(data = this).bytes)

    fun hex(): String = platformValue.hex()

    constructor(checksum: Digest): this(checksum.bytes) {
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as Data
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = Data(this as MutableStruct)

    override fun toString(): String = description

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object: DecodableCompanion<Data> {
        // Static init until constructor overload works.

        fun contentsOfFile(filePath: String): Data = Data(platformValue = java.io.File(filePath).readBytes())

        // Static init until constructor overload works.
        fun contentsOfURL(url: URL): Data {
            //if url.isFileURL {
            //    return Data(java.io.File(url.path).readBytes())
            //} else {
            //    return Data(url.platformValue.openConnection().getInputStream().readBytes())
            //}

            // this seems to work for both file URLs and network URLs
            return Data(platformValue = url.platformValue.readBytes())
        }

        override fun init(from: Decoder): Data = Data(from = from)
    }
}


internal fun kotlin.ByteArray.swift(nocopy: Boolean = false): Data {
    val platformValue = if (nocopy) this else copyOf()
    return Data(platformValue = platformValue)
}

// Mimic a String constructor.
fun String(data: Data, encoding: StringEncoding): String? = (java.lang.String(data.platformValue, encoding.rawValue) as kotlin.String?).sref()
/// The UTF8-encoded data for this string

val String.utf8Data: Data
    get() = data(using = StringEncoding.utf8) ?: Data()


public operator fun String.Companion.invoke(contentsOf: URL): String { return contentsOf.platformValue.readText() }

public operator fun Data.Companion.invoke(contentsOf: URL): Data { return Data.contentsOfURL(url = contentsOf) }

