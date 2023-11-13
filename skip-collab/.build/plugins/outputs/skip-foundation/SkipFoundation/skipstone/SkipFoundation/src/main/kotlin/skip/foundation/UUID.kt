// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformUUID = java.util.UUID
typealias NSUUID = UUID

class UUID: Codable, MutableStruct {
    internal var platformValue: java.util.UUID
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(uuidString: String) {
        val uuid_0 = try { java.util.UUID.fromString(uuidString) } catch (_: Throwable) { null }
        if (uuid_0 == null) {
            throw NullReturnException()
        }
        this.platformValue = uuid_0
    }

    internal constructor(platformValue: java.util.UUID) {
        this.platformValue = platformValue
    }


    constructor() {
        this.platformValue = java.util.UUID.randomUUID()
    }

    constructor(from: Decoder) {
        val decoder = from
        var container = decoder.singleValueContainer()
        this.platformValue = java.util.UUID.fromString(container.decode(String::class))
    }

    override fun encode(to: Encoder) {
        val encoder = to
        var container = encoder.singleValueContainer()
        container.encode(this.uuidString)
    }


    val uuidString: String
        get() {
            // java.util.UUID is lowercase, Foundation.UUID is uppercase
            return platformValue.toString().uppercase()
        }

    val description: String
        get() = uuidString

    fun kotlin(nocopy: Boolean = false): java.util.UUID = platformValue

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as UUID
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = UUID(this as MutableStruct)

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is UUID) return false
        return platformValue == other.platformValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object: DecodableCompanion<UUID> {
        fun fromString(uuidString: String): UUID? {
            // Java throws an exception for bad UUID, but Foundation expects it to return nil
            // return try? UUID(platformValue: PlatformUUID.fromString(uuidString)) // mistranspiles to: (PlatformUUID.companionObjectInstance as java.util.UUID.Companion).fromString(uuidString))
            return try { UUID(platformValue = java.util.UUID.fromString(uuidString)) } catch (_: Throwable) { null }
        }

        override fun init(from: Decoder): UUID = UUID(from = from)
    }
}


public fun UUID(mostSigBits: Long, leastSigBits: Long): UUID { return UUID(java.util.UUID(mostSigBits, leastSigBits)) }

//extension UUID {
//    public init(mostSigBits: Int64, leastSigBits: Int64) {
//        UUID(mostSigBits, leastSigBits)
//    }
//
//    public var uuidString: String {
//        // java.util.UUID is lowercase, Foundation.UUID is uppercase
//        return platformValue.toString().uppercase()
//    }
//
//    public var description: String {
//        return uuidString
//    }
//}



internal fun java.util.UUID.swift(nocopy: Boolean = false): UUID = UUID(platformValue = this)

