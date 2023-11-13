// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.

open class URLSessionConfiguration {


    open var identifier: String? = null
    //public var requestCachePolicy: NSURLRequest.CachePolicy
    open var timeoutIntervalForRequest: Double = 60.0
    open var timeoutIntervalForResource: Double = 604800.0
    //public var networkServiceType: NSURLRequest.NetworkServiceType
    open var allowsCellularAccess: Boolean = true
    open var allowsExpensiveNetworkAccess: Boolean = true
    open var allowsConstrainedNetworkAccess: Boolean = true
    //public var requiresDNSSECValidation: Bool = true
    open var waitsForConnectivity: Boolean = false
    open var isDiscretionary: Boolean = false
    open var sharedContainerIdentifier: String? = null
    open var sessionSendsLaunchEvents: Boolean = false
    open var connectionProxyDictionary: Dictionary<AnyHashable, Any>? = null
        get() = field.sref({ this.connectionProxyDictionary = it })
        set(newValue) {
            field = newValue.sref()
        }
    //public var tlsMinimumSupportedProtocol: SSLProtocol
    //public var tlsMaximumSupportedProtocol: SSLProtocol
    //public var tlsMinimumSupportedProtocolVersion: tls_protocol_version_t
    //public var tlsMaximumSupportedProtocolVersion: tls_protocol_version_t
    open var httpShouldUsePipelining: Boolean = false
    open var httpShouldSetCookies: Boolean = true
    //public var httpCookieAcceptPolicy: HTTPCookie.AcceptPolicy
    open var httpAdditionalHeaders: Dictionary<AnyHashable, Any>? = null
        get() = field.sref({ this.httpAdditionalHeaders = it })
        set(newValue) {
            field = newValue.sref()
        }
    open var httpMaximumConnectionsPerHost: Int = 6
    //public var httpCookieStorage: HTTPCookieStorage?
    //public var urlCredentialStorage: URLCredentialStorage?
    //public var urlCache: URLCache?
    open var shouldUseExtendedBackgroundIdleMode: Boolean = false
    //public var protocolClasses: [AnyClass]?

    constructor() {
    }

    companion object {
        private val _default = URLSessionConfiguration()

        internal val default: URLSessionConfiguration
            get() = _default
        // TODO: ephemeral config
        private val _ephemeral = URLSessionConfiguration()

        val ephemeral: URLSessionConfiguration
            get() = _ephemeral
    }
}

internal interface HTTPCookieStorage {
}

internal open class URLCache {
    enum class StoragePolicy {
        allowed,
        allowedInMemoryOnly,
        notAllowed;

        companion object {
        }
    }
}

internal interface CachedURLResponse {
}
