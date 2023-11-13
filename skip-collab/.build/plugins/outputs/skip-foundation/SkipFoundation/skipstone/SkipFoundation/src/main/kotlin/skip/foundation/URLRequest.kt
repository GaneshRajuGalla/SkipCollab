// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array
import skip.lib.Set

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformURLRequest = java.net.HttpURLConnection
typealias NSURLRequest = URLRequest

class URLRequest: MutableStruct {
    var url: URL? = null
        get() = field.sref({ this.url = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var httpMethod: String? = "GET"
        set(newValue) {
            willmutate()
            try {
                field = newValue
                if (!suppresssideeffects) {
                    httpMethod?.let { method ->
                        if ((method != method.uppercased()) && arrayOf("GET", "PUT", "HEAD", "POST", "DELETE", "CONNECT").contains(method.uppercased())) {
                            // standard method names are always uppercase
                            this.httpMethod = method.uppercased()
                        }
                    }
                }
            } finally {
                didmutate()
            }
        }
    var httpBody: Data? = null
        get() = field.sref({ this.httpBody = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var allHTTPHeaderFields: Dictionary<String, String>? = null
        get() = field.sref({ this.allHTTPHeaderFields = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var cachePolicy: URLRequest.CachePolicy = CachePolicy.useProtocolCachePolicy
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var timeoutInterval: Double = 0.0
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var allowsCellularAccess: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var allowsExpensiveNetworkAccess: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var allowsConstrainedNetworkAccess: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var assumesHTTP3Capable: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var requiresDNSSECValidation: Boolean = false
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var httpShouldHandleCookies: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var httpShouldUsePipelining: Boolean = true
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var mainDocumentURL: URL? = null
        get() = field.sref({ this.mainDocumentURL = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    //public var networkServiceType: URLRequest.NetworkServiceType
    //public var attribution: URLRequest.Attribution
    //public var httpBodyStream: InputStream?


    constructor(url: URL, cachePolicy: URLRequest.CachePolicy = CachePolicy.useProtocolCachePolicy, timeoutInterval: Double = 0.0) {
        suppresssideeffects = true
        try {
            this.url = url
            this.cachePolicy = cachePolicy
            this.timeoutInterval = timeoutInterval
        } finally {
            suppresssideeffects = false
        }
    }

    val description: String
        get() = url?.toString() ?: "url: nil"

    fun value(forHTTPHeaderField: String): String? {
        val field = forHTTPHeaderField
        return Companion.value(forHTTPHeaderField = field, in_ = allHTTPHeaderFields ?: dictionaryOf())
    }

    private fun transformHeaderKey(value: String): String {
        val lowerName = value.lowercased()
        if (lowerName == "accept") {
            return "Accept"
        }
        return value
    }

    fun setValue(value: String?, forHTTPHeaderField: String) {
        val field = forHTTPHeaderField
        willmutate()
        try {
            if (Companion.reservedHeaderKeys.contains(field)) {
                return // ignore reserved keys
            }
            var fields = (this.allHTTPHeaderFields ?: dictionaryOf()).sref()
            fields[transformHeaderKey(field)] = value
            this.allHTTPHeaderFields = fields
        } finally {
            didmutate()
        }
    }

    fun addValue(value: String, forHTTPHeaderField: String) {
        val field = forHTTPHeaderField
        willmutate()
        try {
            if (Companion.reservedHeaderKeys.contains(field)) {
                return // ignore reserved keys
            }
            val fieldKey = transformHeaderKey(field)
            var fields = (this.allHTTPHeaderFields ?: dictionaryOf()).sref()
            var fieldValue: String = value
            // multiple vales are appended together with commas
            fields[fieldKey]?.let { existingValue ->
                if (!existingValue.isEmpty && !value.isEmpty) {
                    fieldValue = existingValue + "," + value
                }
            }
            fields[fieldKey] = fieldValue
            this.allHTTPHeaderFields = fields
        } finally {
            didmutate()
        }
    }

    enum class CachePolicy(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
        useProtocolCachePolicy(0),
        reloadIgnoringLocalCacheData(1),
        reloadIgnoringLocalAndRemoteCacheData(4),
        //public static var reloadIgnoringCacheData: NSURLRequest.CachePolicy { get }
        returnCacheDataElseLoad(2),
        returnCacheDataDontLoad(3),
        reloadRevalidatingCacheData(5);

        companion object {
        }
    }

    fun CachePolicy(rawValue: Int): URLRequest.CachePolicy? {
        return when (rawValue) {
            0 -> CachePolicy.useProtocolCachePolicy
            1 -> CachePolicy.reloadIgnoringLocalCacheData
            4 -> CachePolicy.reloadIgnoringLocalAndRemoteCacheData
            2 -> CachePolicy.returnCacheDataElseLoad
            3 -> CachePolicy.returnCacheDataDontLoad
            5 -> CachePolicy.reloadRevalidatingCacheData
            else -> null
        }
    }

    private constructor(copy: MutableStruct) {
        suppresssideeffects = true
        try {
            @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as URLRequest
            this.url = copy.url
            this.httpMethod = copy.httpMethod
            this.httpBody = copy.httpBody
            this.allHTTPHeaderFields = copy.allHTTPHeaderFields
            this.cachePolicy = copy.cachePolicy
            this.timeoutInterval = copy.timeoutInterval
            this.allowsCellularAccess = copy.allowsCellularAccess
            this.allowsExpensiveNetworkAccess = copy.allowsExpensiveNetworkAccess
            this.allowsConstrainedNetworkAccess = copy.allowsConstrainedNetworkAccess
            this.assumesHTTP3Capable = copy.assumesHTTP3Capable
            this.requiresDNSSECValidation = copy.requiresDNSSECValidation
            this.httpShouldHandleCookies = copy.httpShouldHandleCookies
            this.httpShouldUsePipelining = copy.httpShouldUsePipelining
            this.mainDocumentURL = copy.mainDocumentURL
        } finally {
            suppresssideeffects = false
        }
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = URLRequest(this as MutableStruct)

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is URLRequest) return false
        return url == other.url && httpMethod == other.httpMethod && httpBody == other.httpBody && allHTTPHeaderFields == other.allHTTPHeaderFields && cachePolicy == other.cachePolicy && timeoutInterval == other.timeoutInterval && allowsCellularAccess == other.allowsCellularAccess && allowsExpensiveNetworkAccess == other.allowsExpensiveNetworkAccess && allowsConstrainedNetworkAccess == other.allowsConstrainedNetworkAccess && assumesHTTP3Capable == other.assumesHTTP3Capable && requiresDNSSECValidation == other.requiresDNSSECValidation && httpShouldHandleCookies == other.httpShouldHandleCookies && httpShouldUsePipelining == other.httpShouldUsePipelining && mainDocumentURL == other.mainDocumentURL
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, url)
        result = Hasher.combine(result, httpMethod)
        result = Hasher.combine(result, httpBody)
        result = Hasher.combine(result, allHTTPHeaderFields)
        result = Hasher.combine(result, cachePolicy)
        result = Hasher.combine(result, timeoutInterval)
        result = Hasher.combine(result, allowsCellularAccess)
        result = Hasher.combine(result, allowsExpensiveNetworkAccess)
        result = Hasher.combine(result, allowsConstrainedNetworkAccess)
        result = Hasher.combine(result, assumesHTTP3Capable)
        result = Hasher.combine(result, requiresDNSSECValidation)
        result = Hasher.combine(result, httpShouldHandleCookies)
        result = Hasher.combine(result, httpShouldUsePipelining)
        result = Hasher.combine(result, mainDocumentURL)
        return result
    }

    private var suppresssideeffects = false

    companion object {

        /// Perform a case-insensitive header lookup for the given field name in the header fields
        internal fun value(forHTTPHeaderField: String, in_: Dictionary<String, String>): String? {
            val fieldName = forHTTPHeaderField
            val headerFields = in_
            val matchtarget_0 = headerFields[fieldName]
            if (matchtarget_0 != null) {
                val value = matchtarget_0
                // fast case-sensitive match
                return value.description
            } else {
                // case-insensitive key lookup
                val fieldKey = fieldName.lowercased()
                for ((key, value) in headerFields.sref()) {
                    if (fieldKey == key.lowercased()) {
                        return value
                    }
                }
                return null // not found
            }
        }

        // The lowercased header heys that are reserved
        private val reservedHeaderKeys = Set(arrayOf(
            "Content-Length".lowercased(),
            "Authorization".lowercased(),
            "Connection".lowercased(),
            "Host".lowercased(),
            "Proxy-Authenticate".lowercased(),
            "Proxy-Authorization".lowercased(),
            "WWW-Authenticate".lowercased()
        ))
    }
}
