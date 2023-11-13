// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias MessageDigest = java.security.MessageDigest


interface Digest {
    val bytes: kotlin.ByteArray
}

interface HashFunction {
    fun update(data: DataProtocol)
    fun finalize(): Digest
}

interface NamedHashFunction<Digest>: HashFunction {
    val digest: java.security.MessageDigest
    val digestName: String // Kotlin does not support static members in protocols
}

class SHA256: NamedHashFunction<SHA256Digest> {
    override val digest: java.security.MessageDigest = java.security.MessageDigest.getInstance("SHA-256")
    override val digestName = "SHA256"

    override fun update(data: DataProtocol): Unit = digest.update(data.platformData)

    override fun finalize(): SHA256Digest = SHA256Digest(bytes = digest.digest())

    companion object {

        fun hash(data: Data): SHA256Digest = SHA256Digest(bytes = SHA256().digest.digest(data.platformValue))
    }
}

class SHA256Digest: Digest {
    override val bytes: kotlin.ByteArray

    internal val description: String
        get() = "SHA256 digest: " + bytes.hex()

    constructor(bytes: kotlin.ByteArray) {
        this.bytes = bytes
    }

    companion object {
    }
}

class SHA384: NamedHashFunction<SHA384Digest> {
    override val digest: java.security.MessageDigest = java.security.MessageDigest.getInstance("SHA-384")
    override val digestName = "SHA384"

    override fun update(data: DataProtocol): Unit = digest.update(data.platformData)

    override fun finalize(): SHA384Digest = SHA384Digest(bytes = digest.digest())

    companion object {

        fun hash(data: Data): SHA384Digest = SHA384Digest(bytes = SHA384().digest.digest(data.platformValue))
    }
}

class SHA384Digest: Digest {
    override val bytes: kotlin.ByteArray

    internal val description: String
        get() = "SHA384 digest: " + bytes.hex()

    constructor(bytes: kotlin.ByteArray) {
        this.bytes = bytes
    }

    companion object {
    }
}

class SHA512: NamedHashFunction<SHA512Digest> {
    override val digest: java.security.MessageDigest = java.security.MessageDigest.getInstance("SHA-512")
    override val digestName = "SHA"

    override fun update(data: DataProtocol): Unit = digest.update(data.platformData)

    override fun finalize(): SHA512Digest = SHA512Digest(bytes = digest.digest())

    companion object {

        fun hash(data: Data): SHA512Digest = SHA512Digest(bytes = SHA512().digest.digest(data.platformValue))
    }
}

class SHA512Digest: Digest {
    override val bytes: kotlin.ByteArray

    internal val description: String
        get() = "SHA512 digest: " + bytes.hex()

    constructor(bytes: kotlin.ByteArray) {
        this.bytes = bytes
    }

    companion object {
    }
}

class Insecure {
    class MD5: NamedHashFunction<Insecure.MD5Digest> {
        override val digest: java.security.MessageDigest = java.security.MessageDigest.getInstance("MD5")
        override val digestName = "MD5"

        override fun update(data: DataProtocol): Unit = digest.update(data.platformData)

        override fun finalize(): Insecure.MD5Digest = MD5Digest(bytes = digest.digest())

        companion object {

            fun hash(data: Data): Insecure.MD5Digest = MD5Digest(bytes = MD5().digest.digest(data.platformValue))
        }
    }

    class MD5Digest: Digest {
        override val bytes: kotlin.ByteArray

        internal val description: String
            get() = "MD5 digest: " + bytes.hex()

        constructor(bytes: kotlin.ByteArray) {
            this.bytes = bytes
        }

        companion object {
        }
    }

    class SHA1: NamedHashFunction<Insecure.SHA1Digest> {
        override val digest: java.security.MessageDigest = java.security.MessageDigest.getInstance("SHA1")
        override val digestName = "SHA1"

        override fun update(data: DataProtocol): Unit = digest.update(data.platformData)

        override fun finalize(): Insecure.SHA1Digest = SHA1Digest(bytes = digest.digest())

        companion object {

            fun hash(data: Data): Insecure.SHA1Digest = SHA1Digest(bytes = SHA1().digest.digest(data.platformValue))
        }
    }

    class SHA1Digest: Digest {
        override val bytes: kotlin.ByteArray

        internal val description: String
            get() = "SHA1 digest: " + bytes.hex()

        constructor(bytes: kotlin.ByteArray) {
            this.bytes = bytes
        }

        companion object {
        }
    }

    companion object {
    }
}

// Implemented as a simple Data wrapper.
class SymmetricKey {
    internal val data: Data

    internal constructor(data: Data) {
        this.data = data.sref()
    }

    companion object {
    }
}

open class HMACMD5: DigestFunction() {

    companion object {
        fun authenticationCode(for_: Data, using: SymmetricKey): kotlin.ByteArray {
            val message = for_
            val secret = using
            return DigestFunction.authenticationCode(for_ = message, using = secret, algorithm = "MD5")
        }
    }
}

open class HMACSHA1: DigestFunction() {

    companion object {
        fun authenticationCode(for_: Data, using: SymmetricKey): kotlin.ByteArray {
            val message = for_
            val secret = using
            return DigestFunction.authenticationCode(for_ = message, using = secret, algorithm = "SHA1")
        }
    }
}

open class HMACSHA256: DigestFunction() {

    companion object {
        fun authenticationCode(for_: Data, using: SymmetricKey): kotlin.ByteArray {
            val message = for_
            val secret = using
            return DigestFunction.authenticationCode(for_ = message, using = secret, algorithm = "SHA256")
        }
    }
}

open class HMACSHA384: DigestFunction() {

    companion object {
        fun authenticationCode(for_: Data, using: SymmetricKey): kotlin.ByteArray {
            val message = for_
            val secret = using
            return DigestFunction.authenticationCode(for_ = message, using = secret, algorithm = "SHA384")
        }
    }
}

open class HMACSHA512: DigestFunction() {

    companion object {
        fun authenticationCode(for_: Data, using: SymmetricKey): kotlin.ByteArray {
            val message = for_
            val secret = using
            return DigestFunction.authenticationCode(for_ = message, using = secret, algorithm = "SHA512")
        }
    }
}

fun kotlin.ByteArray.hex(): String {
    return joinToString("") { it -> java.lang.Byte.toUnsignedInt(it).toString(radix = 16).padStart(2, "0".get(0)) }
}

open class DigestFunction {

    companion object {
        internal fun authenticationCode(for_: Data, using: SymmetricKey, algorithm: String): kotlin.ByteArray {
            val message = for_
            val secret = using
            val hashName = algorithm
            val secretKeySpec = javax.crypto.spec.SecretKeySpec(secret.data.platformValue, "Hmac${hashName}")
            val mac = javax.crypto.Mac.getInstance("Hmac${hashName}")
            // Skip removes .init because it assumes you want a constructor, so we need to put it back in
            mac.init(secretKeySpec)
            val signature = mac.doFinal(message.platformValue)
            return signature
        }
    }
}

