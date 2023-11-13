// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

// This code is adapted from https://github.com/apple/swift-corelibs-foundation/blob/main/Tests/Foundation/Tests which has the following license:

//===----------------------------------------------------------------------===//
//
// This source file is part of the Swift.org open source project
//
// Copyright (c) 2014 - 2021 Apple Inc. and the Swift project authors
// Licensed under Apache License v2.0 with Runtime Library Exception
//
// See https://swift.org/LICENSE.txt for license information
// See https://swift.org/CONTRIBUTORS.txt for the list of Swift project authors
//
//===----------------------------------------------------------------------===//

//@_implementationOnly import CoreFoundation

typealias NSErrorDomain = String

/// Predefined domain for errors from most Foundation APIs.
val NSCocoaErrorDomain: String = "NSCocoaErrorDomain"

// Other predefined domains; value of "code" will correspond to preexisting values in these domains.
val NSPOSIXErrorDomain: String = "NSPOSIXErrorDomain"
val NSOSStatusErrorDomain: String = "NSOSStatusErrorDomain"
val NSMachErrorDomain: String = "NSMachErrorDomain"

// Key in userInfo. A recommended standard way to embed NSErrors from underlying calls. The value of this key should be an NSError.
val NSUnderlyingErrorKey: String = "NSUnderlyingError"

// Keys in userInfo, for subsystems wishing to provide their error messages up-front. Note that NSError will also consult the userInfoValueProvider for the domain when these values are not present in the userInfo dictionary.
val NSLocalizedDescriptionKey: String = "NSLocalizedDescription"
val NSLocalizedFailureReasonErrorKey: String = "NSLocalizedFailureReason"
val NSLocalizedRecoverySuggestionErrorKey: String = "NSLocalizedRecoverySuggestion"
val NSLocalizedRecoveryOptionsErrorKey: String = "NSLocalizedRecoveryOptions"
val NSRecoveryAttempterErrorKey: String = "NSRecoveryAttempter"
val NSHelpAnchorErrorKey: String = "NSHelpAnchor"
val NSDebugDescriptionErrorKey = "NSDebugDescription"

// Other standard keys in userInfo, for various error codes
val NSStringEncodingErrorKey: String = "NSStringEncodingErrorKey"
val NSURLErrorKey: String = "NSURL"
val NSFilePathErrorKey: String = "NSFilePathErrorKey"

open class NSError: Exception, Error {
    // ErrorType forbids this being internal
    open var _domain: String
    open var _code: Int
    /// - Experiment: This is a draft API currently under consideration for official import into Foundation.
    /// - Note: This API differs from Darwin because it uses [String : Any] as a type instead of [String : AnyObject]. This allows the use of Swift value types.
    private var _userInfo: Dictionary<String, Any>? = null
        get() = field.sref({ this._userInfo = it })
        set(newValue) {
            field = newValue.sref()
        }

    /// - Experiment: This is a draft API currently under consideration for official import into Foundation.
    /// - Note: This API differs from Darwin because it uses [String : Any] as a type instead of [String : AnyObject]. This allows the use of Swift value types.
    constructor(domain: String, code: Int, userInfo: Dictionary<String, Any>? = null): super() {
        val dict = userInfo
        _domain = domain
        _code = code
        _userInfo = dict
    }

    open val domain: String
        get() = _domain

    open val code: Int
        get() = _code

    /// - Experiment: This is a draft API currently under consideration for official import into Foundation.
    /// - Note: This API differs from Darwin because it uses [String : Any] as a type instead of [String : AnyObject]. This allows the use of Swift value types.
    open val userInfo: Dictionary<String, Any>
        get() {
            val matchtarget_0 = _userInfo
            if (matchtarget_0 != null) {
                val info = matchtarget_0
                return info
            } else {
                return Dictionary<String, Any>()
            }
        }

    open val localizedDescription: String
        get() {
            val matchtarget_1 = userInfo[NSLocalizedDescriptionKey] as? String
            if (matchtarget_1 != null) {
                val localizedDescription = matchtarget_1
                return localizedDescription
            } else {
                // placeholder values
                return "The operation could not be completed." + " " + (this.localizedFailureReason ?: "(${domain} error ${code}.)")
            }
        }

    open val localizedFailureReason: String?
        get() {
            val matchtarget_2 = userInfo[NSLocalizedFailureReasonErrorKey] as? String
            if (matchtarget_2 != null) {
                val localizedFailureReason = matchtarget_2
                return localizedFailureReason
            } else {
                when (domain) {
                    NSPOSIXErrorDomain -> {
                        //return String(cString: strerror(Int32(code)), encoding: .ascii)
                        return "POSIX error ${code}"
                    }
                    NSCocoaErrorDomain -> return CocoaError.errorMessages[CocoaError.Code(rawValue = code)]
                    NSURLErrorDomain -> return null
                    else -> return null
                }
            }
        }

    open val localizedRecoverySuggestion: String?
        get() = userInfo[NSLocalizedRecoverySuggestionErrorKey] as? String

    open val localizedRecoveryOptions: Array<String>?
        get() = userInfo[NSLocalizedRecoveryOptionsErrorKey] as? Array<String>

    open val recoveryAttempter: Any?
        get() = userInfo[NSRecoveryAttempterErrorKey]

    open val helpAnchor: String?
        get() = userInfo[NSHelpAnchorErrorKey] as? String

    open val description: String
        get() = "Error Domain=${domain} Code=${code} \"${localizedFailureReason ?: "(null)"}\""

    override fun toString(): String = description

    companion object {
        internal var userInfoProviders = Dictionary<String, (Error, String) -> Any?>()
            get() = field.sref({ this.userInfoProviders = it })
            set(newValue) {
                field = newValue.sref()
            }

        fun setUserInfoValueProvider(forDomain: String, provider: ((Error, String) -> Any?)?) {
            val errorDomain = forDomain
            NSError.userInfoProviders[errorDomain] = provider
        }

        fun userInfoValueProvider(forDomain: String): ((Error, String) -> Any?)? {
            val errorDomain = forDomain
            return NSError.userInfoProviders[errorDomain]
        }
    }
}

//extension CFError : _NSBridgeable {
//    typealias NSType = NSError
//    internal var _nsObject: NSType {
//        let userInfo = CFErrorCopyUserInfo(self)._swiftObject
//        var newUserInfo: [String: Any] = [:]
//        for (key, value) in userInfo {
//            if let key = key as? String {
//                newUserInfo[key] = value
//            }
//        }
//
//        return NSError(domain: CFErrorGetDomain(self)._swiftObject, code: CFErrorGetCode(self), userInfo: newUserInfo)
//    }
//}
//
//public struct _CFErrorSPIForFoundationXMLUseOnly {
//    let error: AnyObject
//    public init(unsafelyAssumingIsCFError error: AnyObject) {
//        self.error = error
//    }
//
//    public var _nsObject: NSError {
//        return unsafeBitCast(error, to: CFError.self)._nsObject
//    }
//}

/// Describes an error that provides localized messages describing why
/// an error occurred and provides more information about the error.
interface LocalizedError: Error {
    val errorDescription: String?
        get() = null

    val failureReason: String?
        get() = null

    val recoverySuggestion: String?
        get() = null

    val helpAnchor: String?
        get() = null
}

/// Class that implements the informal protocol.
/// NSErrorRecoveryAttempting, which is used by NSError when it
/// attempts recovery from an error.
internal open class _NSErrorRecoveryAttempter {
    internal open fun attemptRecovery(fromError: Error, optionIndex: Int): Boolean {
        val error = fromError
        val recoveryOptionIndex = optionIndex
        val recoverableError = (error as RecoverableError).sref()
        return recoverableError.attemptRecovery(optionIndex = recoveryOptionIndex)
    }
}

/// Describes an error that may be recoverable by presenting several
/// potential recovery options to the user.
interface RecoverableError: Error {
    /// Provides a set of possible recovery options to present to the user.
    val recoveryOptions: Array<String>
    /// Default implementation that uses the application-model recovery
    /// mechanism (``attemptRecovery(optionIndex:)``) to implement
    /// document-modal recovery.

    fun attemptRecovery(optionIndex: Int, resultHandler: (Boolean) -> Unit) {
        val recoveryOptionIndex = optionIndex
        val handler = resultHandler
        handler(attemptRecovery(optionIndex = recoveryOptionIndex))
    }

    /// Attempt to recover from this error when the user selected the
    /// option at the given index. Returns true to indicate
    /// successful recovery, and false otherwise.
    ///
    /// This entry point is used for recovery of errors handled at
    /// the "application" granularity, where nothing else in the
    /// application can proceed until the attempted error recovery
    /// completes.
    fun attemptRecovery(optionIndex: Int): Boolean
}

/// Describes an error type that specifically provides a domain, code,
/// and user-info dictionary.
interface CustomNSError: Error {

    /// The error code within the given domain.
    val errorCode: Int
        get() = 0 // no equivalent for Swift._getDefaultErrorCode()

    /// The default user-info dictionary.
    val errorUserInfo: Dictionary<String, Any>
        get() = dictionaryOf()
}



/// Describes errors within the Cocoa error domain.
class CocoaError {
    val _nsError: NSError

    constructor(_nsError: NSError) {
        val error = _nsError
        precondition(error.domain == NSCocoaErrorDomain)
        this._nsError = error
    }

    val code: CocoaError.Code
        get() = Code(rawValue = _nsError.code)

    /// The error code itself.
    class Code: RawRepresentable<Int> {
        //public typealias _ErrorType = CocoaError

        override val rawValue: Int

        constructor(rawValue: Int) {
            this.rawValue = rawValue
        }

        override fun equals(other: Any?): Boolean {
            if (other !is CocoaError.Code) return false
            return rawValue == other.rawValue
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, rawValue)
            return result
        }

        companion object {

            val fileNoSuchFile: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4)
            val fileLocking: CocoaError.Code
                get() = CocoaError.Code(rawValue = 255)
            val fileReadUnknown: CocoaError.Code
                get() = CocoaError.Code(rawValue = 256)
            val fileReadNoPermission: CocoaError.Code
                get() = CocoaError.Code(rawValue = 257)
            val fileReadInvalidFileName: CocoaError.Code
                get() = CocoaError.Code(rawValue = 258)
            val fileReadCorruptFile: CocoaError.Code
                get() = CocoaError.Code(rawValue = 259)
            val fileReadNoSuchFile: CocoaError.Code
                get() = CocoaError.Code(rawValue = 260)
            val fileReadInapplicableStringEncoding: CocoaError.Code
                get() = CocoaError.Code(rawValue = 261)
            val fileReadUnsupportedScheme: CocoaError.Code
                get() = CocoaError.Code(rawValue = 262)
            val fileReadTooLarge: CocoaError.Code
                get() = CocoaError.Code(rawValue = 263)
            val fileReadUnknownStringEncoding: CocoaError.Code
                get() = CocoaError.Code(rawValue = 264)
            val fileWriteUnknown: CocoaError.Code
                get() = CocoaError.Code(rawValue = 512)
            val fileWriteNoPermission: CocoaError.Code
                get() = CocoaError.Code(rawValue = 513)
            val fileWriteInvalidFileName: CocoaError.Code
                get() = CocoaError.Code(rawValue = 514)
            val fileWriteFileExists: CocoaError.Code
                get() = CocoaError.Code(rawValue = 516)
            val fileWriteInapplicableStringEncoding: CocoaError.Code
                get() = CocoaError.Code(rawValue = 517)
            val fileWriteUnsupportedScheme: CocoaError.Code
                get() = CocoaError.Code(rawValue = 518)
            val fileWriteOutOfSpace: CocoaError.Code
                get() = CocoaError.Code(rawValue = 640)
            val fileWriteVolumeReadOnly: CocoaError.Code
                get() = CocoaError.Code(rawValue = 642)
            val fileManagerUnmountUnknown: CocoaError.Code
                get() = CocoaError.Code(rawValue = 768)
            val fileManagerUnmountBusy: CocoaError.Code
                get() = CocoaError.Code(rawValue = 769)
            val keyValueValidation: CocoaError.Code
                get() = CocoaError.Code(rawValue = 1024)
            val formatting: CocoaError.Code
                get() = CocoaError.Code(rawValue = 2048)
            val userCancelled: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3072)
            val featureUnsupported: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3328)
            val executableNotLoadable: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3584)
            val executableArchitectureMismatch: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3585)
            val executableRuntimeMismatch: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3586)
            val executableLoad: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3587)
            val executableLink: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3588)
            val propertyListReadCorrupt: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3840)
            val propertyListReadUnknownVersion: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3841)
            val propertyListReadStream: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3842)
            val propertyListWriteStream: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3851)
            val propertyListWriteInvalid: CocoaError.Code
                get() = CocoaError.Code(rawValue = 3852)
            val xpcConnectionInterrupted: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4097)
            val xpcConnectionInvalid: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4099)
            val xpcConnectionReplyInvalid: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4101)
            val ubiquitousFileUnavailable: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4353)
            val ubiquitousFileNotUploadedDueToQuota: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4354)
            val ubiquitousFileUbiquityServerNotAvailable: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4355)
            val userActivityHandoffFailed: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4608)
            val userActivityConnectionUnavailable: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4609)
            val userActivityRemoteApplicationTimedOut: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4610)
            val userActivityHandoffUserInfoTooLarge: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4611)
            val coderReadCorrupt: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4864)
            val coderValueNotFound: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4865)
            val coderInvalidValue: CocoaError.Code
                get() = CocoaError.Code(rawValue = 4866)
        }
    }

    private val _nsUserInfo: Dictionary<String, Any>
        get() = _nsError.userInfo

    /// The file path associated with the error, if any.
    val filePath: String?
        get() = _nsUserInfo[NSFilePathErrorKey] as? String


    /// The underlying error behind this error, if any.
    val underlying: Error?
        get() = _nsUserInfo[NSUnderlyingErrorKey] as? Error

    /// The URL associated with this error, if any.
    val url: URL?
        get() = _nsUserInfo[NSURLErrorKey] as? URL

    val isCoderError: Boolean
        get() = code.rawValue >= 4864 && code.rawValue <= 4991

    val isExecutableError: Boolean
        get() = code.rawValue >= 3584 && code.rawValue <= 3839

    val isFileError: Boolean
        get() = code.rawValue >= 0 && code.rawValue <= 1023

    val isFormattingError: Boolean
        get() = code.rawValue >= 2048 && code.rawValue <= 2559

    val isPropertyListError: Boolean
        get() = code.rawValue >= 3840 && code.rawValue <= 4095

    val isUbiquitousFileError: Boolean
        get() = code.rawValue >= 4352 && code.rawValue <= 4607

    val isUserActivityError: Boolean
        get() = code.rawValue >= 4608 && code.rawValue <= 4863

    val isValidationError: Boolean
        get() = code.rawValue >= 1024 && code.rawValue <= 2047

    val isXPCConnectionError: Boolean
        get() = code.rawValue >= 4096 && code.rawValue <= 4224

    companion object {

        val _nsErrorDomain: String
            get() = NSCocoaErrorDomain

        internal val errorMessages = dictionaryOf(
            Tuple2(Code(rawValue = 4), "The file doesn’t exist."),
            Tuple2(Code(rawValue = 255), "The file couldn’t be locked."),
            Tuple2(Code(rawValue = 257), "You don’t have permission."),
            Tuple2(Code(rawValue = 258), "The file name is invalid."),
            Tuple2(Code(rawValue = 259), "The file isn’t in the correct format."),
            Tuple2(Code(rawValue = 260), "The file doesn’t exist."),
            Tuple2(Code(rawValue = 261), "The specified text encoding isn’t applicable."),
            Tuple2(Code(rawValue = 262), "The specified URL type isn’t supported."),
            Tuple2(Code(rawValue = 263), "The item is too large."),
            Tuple2(Code(rawValue = 264), "The text encoding of the contents couldn’t be determined."),
            Tuple2(Code(rawValue = 513), "You don’t have permission."),
            Tuple2(Code(rawValue = 514), "The file name is invalid."),
            Tuple2(Code(rawValue = 516), "A file with the same name already exists."),
            Tuple2(Code(rawValue = 517), "The specified text encoding isn’t applicable."),
            Tuple2(Code(rawValue = 518), "The specified URL type isn’t supported."),
            Tuple2(Code(rawValue = 640), "There isn’t enough space."),
            Tuple2(Code(rawValue = 642), "The volume is read only."),
            Tuple2(Code(rawValue = 1024), "The value is invalid."),
            Tuple2(Code(rawValue = 2048), "The value is invalid."),
            Tuple2(Code(rawValue = 3072), "The operation was cancelled."),
            Tuple2(Code(rawValue = 3328), "The requested operation is not supported."),
            Tuple2(Code(rawValue = 3840), "The data is not in the correct format."),
            Tuple2(Code(rawValue = 3841), "The data is in a format that this application doesn’t understand."),
            Tuple2(Code(rawValue = 3842), "An error occurred in the source of the data."),
            Tuple2(Code(rawValue = 3851), "An error occurred in the destination for the data."),
            Tuple2(Code(rawValue = 3852), "An error occurred in the content of the data."),
            Tuple2(Code(rawValue = 4353), "The file is not available on iCloud yet."),
            Tuple2(Code(rawValue = 4354), "There isn’t enough space in your account."),
            Tuple2(Code(rawValue = 4355), "The iCloud servers might be unreachable or your settings might be incorrect."),
            Tuple2(Code(rawValue = 4864), "The data isn’t in the correct format."),
            Tuple2(Code(rawValue = 4865), "The data is missing."),
            Tuple2(Code(rawValue = 4866), "The data isn’t in the correct format.")
        )

        fun error(code: CocoaError.Code, userInfo: Dictionary<AnyHashable, Any>? = null, url: URL? = null): Error {
            var info: Dictionary<String, Any> = (userInfo as? Dictionary<String, Any> ?: dictionaryOf()).sref()
            if (url != null) {
                info[NSURLErrorKey] = url.sref()
            }
            return NSError(domain = NSCocoaErrorDomain, code = code.rawValue, userInfo = info)
        }

        val fileNoSuchFile: CocoaError.Code
            get() = CocoaError.Code.fileNoSuchFile
        val fileLocking: CocoaError.Code
            get() = CocoaError.Code.fileLocking
        val fileReadUnknown: CocoaError.Code
            get() = CocoaError.Code.fileReadUnknown
        val fileReadNoPermission: CocoaError.Code
            get() = CocoaError.Code.fileReadNoPermission
        val fileReadInvalidFileName: CocoaError.Code
            get() = CocoaError.Code.fileReadInvalidFileName
        val fileReadCorruptFile: CocoaError.Code
            get() = CocoaError.Code.fileReadCorruptFile
        val fileReadNoSuchFile: CocoaError.Code
            get() = CocoaError.Code.fileReadNoSuchFile
        val fileReadInapplicableStringEncoding: CocoaError.Code
            get() = CocoaError.Code.fileReadInapplicableStringEncoding
        val fileReadUnsupportedScheme: CocoaError.Code
            get() = CocoaError.Code.fileReadUnsupportedScheme
        val fileReadTooLarge: CocoaError.Code
            get() = CocoaError.Code.fileReadTooLarge
        val fileReadUnknownStringEncoding: CocoaError.Code
            get() = CocoaError.Code.fileReadUnknownStringEncoding
        val fileWriteUnknown: CocoaError.Code
            get() = CocoaError.Code.fileWriteUnknown
        val fileWriteNoPermission: CocoaError.Code
            get() = CocoaError.Code.fileWriteNoPermission
        val fileWriteInvalidFileName: CocoaError.Code
            get() = CocoaError.Code.fileWriteInvalidFileName
        val fileWriteFileExists: CocoaError.Code
            get() = CocoaError.Code.fileWriteFileExists
        val fileWriteInapplicableStringEncoding: CocoaError.Code
            get() = CocoaError.Code.fileWriteInapplicableStringEncoding
        val fileWriteUnsupportedScheme: CocoaError.Code
            get() = CocoaError.Code.fileWriteUnsupportedScheme
        val fileWriteOutOfSpace: CocoaError.Code
            get() = CocoaError.Code.fileWriteOutOfSpace
        val fileWriteVolumeReadOnly: CocoaError.Code
            get() = CocoaError.Code.fileWriteVolumeReadOnly
        val fileManagerUnmountUnknown: CocoaError.Code
            get() = CocoaError.Code.fileManagerUnmountUnknown
        val fileManagerUnmountBusy: CocoaError.Code
            get() = CocoaError.Code.fileManagerUnmountBusy
        val keyValueValidation: CocoaError.Code
            get() = CocoaError.Code.keyValueValidation
        val formatting: CocoaError.Code
            get() = CocoaError.Code.formatting
        val userCancelled: CocoaError.Code
            get() = CocoaError.Code.userCancelled
        val featureUnsupported: CocoaError.Code
            get() = CocoaError.Code.featureUnsupported
        val executableNotLoadable: CocoaError.Code
            get() = CocoaError.Code.executableNotLoadable
        val executableArchitectureMismatch: CocoaError.Code
            get() = CocoaError.Code.executableArchitectureMismatch
        val executableRuntimeMismatch: CocoaError.Code
            get() = CocoaError.Code.executableRuntimeMismatch
        val executableLoad: CocoaError.Code
            get() = CocoaError.Code.executableLoad
        val executableLink: CocoaError.Code
            get() = CocoaError.Code.executableLink
        val propertyListReadCorrupt: CocoaError.Code
            get() = CocoaError.Code.propertyListReadCorrupt
        val propertyListReadUnknownVersion: CocoaError.Code
            get() = CocoaError.Code.propertyListReadUnknownVersion
        val propertyListReadStream: CocoaError.Code
            get() = CocoaError.Code.propertyListReadStream
        val propertyListWriteStream: CocoaError.Code
            get() = CocoaError.Code.propertyListWriteStream
        val propertyListWriteInvalid: CocoaError.Code
            get() = CocoaError.Code.propertyListWriteInvalid
        val xpcConnectionInterrupted: CocoaError.Code
            get() = CocoaError.Code.xpcConnectionInterrupted
        val xpcConnectionInvalid: CocoaError.Code
            get() = CocoaError.Code.xpcConnectionInvalid
        val xpcConnectionReplyInvalid: CocoaError.Code
            get() = CocoaError.Code.xpcConnectionReplyInvalid
        val ubiquitousFileUnavailable: CocoaError.Code
            get() = CocoaError.Code.ubiquitousFileUnavailable
        val ubiquitousFileNotUploadedDueToQuota: CocoaError.Code
            get() = CocoaError.Code.ubiquitousFileNotUploadedDueToQuota
        val ubiquitousFileUbiquityServerNotAvailable: CocoaError.Code
            get() = CocoaError.Code.ubiquitousFileUbiquityServerNotAvailable
        val userActivityHandoffFailed: CocoaError.Code
            get() = CocoaError.Code.userActivityHandoffFailed
        val userActivityConnectionUnavailable: CocoaError.Code
            get() = CocoaError.Code.userActivityConnectionUnavailable
        val userActivityRemoteApplicationTimedOut: CocoaError.Code
            get() = CocoaError.Code.userActivityRemoteApplicationTimedOut
        val userActivityHandoffUserInfoTooLarge: CocoaError.Code
            get() = CocoaError.Code.userActivityHandoffUserInfoTooLarge
        val coderReadCorrupt: CocoaError.Code
            get() = CocoaError.Code.coderReadCorrupt
        val coderValueNotFound: CocoaError.Code
            get() = CocoaError.Code.coderValueNotFound
        val coderInvalidValue: CocoaError.Code
            get() = CocoaError.Code.coderInvalidValue
    }
}

/// Describes errors in the URL error domain.
class URLError {
    val _nsError: NSError

    constructor(_nsError: NSError) {
        val error = _nsError
        precondition(error.domain == NSURLErrorDomain)
        this._nsError = error
    }

    val code: URLError.Code
        get() = Code(rawValue = _nsError.code)!!

    enum class Code(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
        //public typealias _ErrorType = URLError

        unknown(-1),
        cancelled(-999),
        badURL(-1000),
        timedOut(-1001),
        unsupportedURL(-1002),
        cannotFindHost(-1003),
        cannotConnectToHost(-1004),
        networkConnectionLost(-1005),
        dnsLookupFailed(-1006),
        httpTooManyRedirects(-1007),
        resourceUnavailable(-1008),
        notConnectedToInternet(-1009),
        redirectToNonExistentLocation(-1010),
        badServerResponse(-1011),
        userCancelledAuthentication(-1012),
        userAuthenticationRequired(-1013),
        zeroByteResource(-1014),
        cannotDecodeRawData(-1015),
        cannotDecodeContentData(-1016),
        cannotParseResponse(-1017),
        appTransportSecurityRequiresSecureConnection(-1022),
        fileDoesNotExist(-1100),
        fileIsDirectory(-1101),
        noPermissionsToReadFile(-1102),
        dataLengthExceedsMaximum(-1103),
        secureConnectionFailed(-1200),
        serverCertificateHasBadDate(-1201),
        serverCertificateUntrusted(-1202),
        serverCertificateHasUnknownRoot(-1203),
        serverCertificateNotYetValid(-1204),
        clientCertificateRejected(-1205),
        clientCertificateRequired(-1206),
        cannotLoadFromNetwork(-2000),
        cannotCreateFile(-3000),
        cannotOpenFile(-3001),
        cannotCloseFile(-3002),
        cannotWriteToFile(-3003),
        cannotRemoveFile(-3004),
        cannotMoveFile(-3005),
        downloadDecodingFailedMidStream(-3006),
        downloadDecodingFailedToComplete(-3007),
        internationalRoamingOff(-1018),
        callIsActive(-1019),
        dataNotAllowed(-1020),
        requestBodyStreamExhausted(-1021),
        backgroundSessionRequiresSharedContainer(-995),
        backgroundSessionInUseByAnotherProcess(-996),
        backgroundSessionWasDisconnected(-997);

        companion object {
        }
    }

    fun Code(rawValue: Int): URLError.Code? {
        return when (rawValue) {
            -1 -> Code.unknown
            -999 -> Code.cancelled
            -1000 -> Code.badURL
            -1001 -> Code.timedOut
            -1002 -> Code.unsupportedURL
            -1003 -> Code.cannotFindHost
            -1004 -> Code.cannotConnectToHost
            -1005 -> Code.networkConnectionLost
            -1006 -> Code.dnsLookupFailed
            -1007 -> Code.httpTooManyRedirects
            -1008 -> Code.resourceUnavailable
            -1009 -> Code.notConnectedToInternet
            -1010 -> Code.redirectToNonExistentLocation
            -1011 -> Code.badServerResponse
            -1012 -> Code.userCancelledAuthentication
            -1013 -> Code.userAuthenticationRequired
            -1014 -> Code.zeroByteResource
            -1015 -> Code.cannotDecodeRawData
            -1016 -> Code.cannotDecodeContentData
            -1017 -> Code.cannotParseResponse
            -1022 -> Code.appTransportSecurityRequiresSecureConnection
            -1100 -> Code.fileDoesNotExist
            -1101 -> Code.fileIsDirectory
            -1102 -> Code.noPermissionsToReadFile
            -1103 -> Code.dataLengthExceedsMaximum
            -1200 -> Code.secureConnectionFailed
            -1201 -> Code.serverCertificateHasBadDate
            -1202 -> Code.serverCertificateUntrusted
            -1203 -> Code.serverCertificateHasUnknownRoot
            -1204 -> Code.serverCertificateNotYetValid
            -1205 -> Code.clientCertificateRejected
            -1206 -> Code.clientCertificateRequired
            -2000 -> Code.cannotLoadFromNetwork
            -3000 -> Code.cannotCreateFile
            -3001 -> Code.cannotOpenFile
            -3002 -> Code.cannotCloseFile
            -3003 -> Code.cannotWriteToFile
            -3004 -> Code.cannotRemoveFile
            -3005 -> Code.cannotMoveFile
            -3006 -> Code.downloadDecodingFailedMidStream
            -3007 -> Code.downloadDecodingFailedToComplete
            -1018 -> Code.internationalRoamingOff
            -1019 -> Code.callIsActive
            -1020 -> Code.dataNotAllowed
            -1021 -> Code.requestBodyStreamExhausted
            -995 -> Code.backgroundSessionRequiresSharedContainer
            -996 -> Code.backgroundSessionInUseByAnotherProcess
            -997 -> Code.backgroundSessionWasDisconnected
            else -> null
        }
    }

    private val _nsUserInfo: Dictionary<String, Any>
        get() = _nsError.userInfo

    /// The URL which caused a load to fail.
    val failingURL: URL?
        get() = _nsUserInfo[NSURLErrorFailingURLErrorKey] as? URL

    /// The string for the URL which caused a load to fail.
    val failureURLString: String?
        get() = _nsUserInfo[NSURLErrorFailingURLStringErrorKey] as? String

    companion object {

        val _nsErrorDomain: String
            get() = NSURLErrorDomain

        val unknown: URLError.Code
            get() = URLError.Code.unknown
        val cancelled: URLError.Code
            get() = URLError.Code.cancelled
        val badURL: URLError.Code
            get() = URLError.Code.badURL
        val timedOut: URLError.Code
            get() = URLError.Code.timedOut
        val unsupportedURL: URLError.Code
            get() = URLError.Code.unsupportedURL
        val cannotFindHost: URLError.Code
            get() = URLError.Code.cannotFindHost
        val cannotConnectToHost: URLError.Code
            get() = URLError.Code.cannotConnectToHost
        val networkConnectionLost: URLError.Code
            get() = URLError.Code.networkConnectionLost
        val dnsLookupFailed: URLError.Code
            get() = URLError.Code.dnsLookupFailed
        val httpTooManyRedirects: URLError.Code
            get() = URLError.Code.httpTooManyRedirects
        val resourceUnavailable: URLError.Code
            get() = URLError.Code.resourceUnavailable
        val notConnectedToInternet: URLError.Code
            get() = URLError.Code.notConnectedToInternet
        val redirectToNonExistentLocation: URLError.Code
            get() = URLError.Code.redirectToNonExistentLocation
        val badServerResponse: URLError.Code
            get() = URLError.Code.badServerResponse
        val userCancelledAuthentication: URLError.Code
            get() = URLError.Code.userCancelledAuthentication
        val userAuthenticationRequired: URLError.Code
            get() = URLError.Code.userAuthenticationRequired
        val zeroByteResource: URLError.Code
            get() = URLError.Code.zeroByteResource
        val cannotDecodeRawData: URLError.Code
            get() = URLError.Code.cannotDecodeRawData
        val cannotDecodeContentData: URLError.Code
            get() = URLError.Code.cannotDecodeContentData
        val cannotParseResponse: URLError.Code
            get() = URLError.Code.cannotParseResponse
        val fileDoesNotExist: URLError.Code
            get() = URLError.Code.fileDoesNotExist
        val fileIsDirectory: URLError.Code
            get() = URLError.Code.fileIsDirectory
        val noPermissionsToReadFile: URLError.Code
            get() = URLError.Code.noPermissionsToReadFile
        val secureConnectionFailed: URLError.Code
            get() = URLError.Code.secureConnectionFailed
        val serverCertificateHasBadDate: URLError.Code
            get() = URLError.Code.serverCertificateHasBadDate
        val serverCertificateUntrusted: URLError.Code
            get() = URLError.Code.serverCertificateUntrusted
        val serverCertificateHasUnknownRoot: URLError.Code
            get() = URLError.Code.serverCertificateHasUnknownRoot
        val serverCertificateNotYetValid: URLError.Code
            get() = URLError.Code.serverCertificateNotYetValid
        val clientCertificateRejected: URLError.Code
            get() = URLError.Code.clientCertificateRejected
        val clientCertificateRequired: URLError.Code
            get() = URLError.Code.clientCertificateRequired
        val cannotLoadFromNetwork: URLError.Code
            get() = URLError.Code.cannotLoadFromNetwork
        val cannotCreateFile: URLError.Code
            get() = URLError.Code.cannotCreateFile
        val cannotOpenFile: URLError.Code
            get() = URLError.Code.cannotOpenFile
        val cannotCloseFile: URLError.Code
            get() = URLError.Code.cannotCloseFile
        val cannotWriteToFile: URLError.Code
            get() = URLError.Code.cannotWriteToFile
        val cannotRemoveFile: URLError.Code
            get() = URLError.Code.cannotRemoveFile
        val cannotMoveFile: URLError.Code
            get() = URLError.Code.cannotMoveFile
        val downloadDecodingFailedMidStream: URLError.Code
            get() = URLError.Code.downloadDecodingFailedMidStream
        val downloadDecodingFailedToComplete: URLError.Code
            get() = URLError.Code.downloadDecodingFailedToComplete
        val internationalRoamingOff: URLError.Code
            get() = URLError.Code.internationalRoamingOff
        val callIsActive: URLError.Code
            get() = URLError.Code.callIsActive
        val dataNotAllowed: URLError.Code
            get() = URLError.Code.dataNotAllowed
        val requestBodyStreamExhausted: URLError.Code
            get() = URLError.Code.requestBodyStreamExhausted
        val backgroundSessionRequiresSharedContainer: URLError.Code
            get() = URLError.Code.backgroundSessionRequiresSharedContainer
        val backgroundSessionInUseByAnotherProcess: URLError.Code
            get() = URLError.Code.backgroundSessionInUseByAnotherProcess
        val backgroundSessionWasDisconnected: URLError.Code
            get() = URLError.Code.backgroundSessionWasDisconnected
    }
}

typealias POSIXErrorCode = POSIXError.Code

/// Describes an error in the POSIX error domain.
class POSIXError {

    val _nsError: NSError

    constructor(_nsError: NSError) {
        val error = _nsError
        precondition(error.domain == NSPOSIXErrorDomain)
        this._nsError = error
    }

    //public typealias Code = POSIXErrorCode // FIXME: Kotlin does not support typealias declarations within functions and types. Consider moving this to a top level declaration

    enum class Code(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
        //public typealias RawValue = Int32

        /// Operation not permitted.
        EPERM(1),

        /// No such file or directory.
        ENOENT(2),

        /// No such process.
        ESRCH(3),

        /// Interrupted system call.
        EINTR(4),

        /// Input/output error.
        EIO(5),

        /// Device not configured.
        ENXIO(6),

        /// Argument list too long.
        E2BIG(7),

        /// Exec format error.
        ENOEXEC(8),

        /// Bad file descriptor.
        EBADF(9),

        /// No child processes.
        ECHILD(10),

        /// Resource deadlock avoided.
        EDEADLK(11),

        /// 11 was EAGAIN.
        /// Cannot allocate memory.
        ENOMEM(12),

        /// Permission denied.
        EACCES(13),

        /// Bad address.
        EFAULT(14),

        /// Block device required.
        ENOTBLK(15),

        /// Device / Resource busy.
        EBUSY(16),

        /// File exists.
        EEXIST(17),

        /// Cross-device link.
        EXDEV(18),

        /// Operation not supported by device.
        ENODEV(19),

        /// Not a directory.
        ENOTDIR(20),

        /// Is a directory.
        EISDIR(21),

        /// Invalid argument.
        EINVAL(22),

        /// Too many open files in system.
        ENFILE(23),

        /// Too many open files.
        EMFILE(24),

        /// Inappropriate ioctl for device.
        ENOTTY(25),

        /// Text file busy.
        ETXTBSY(26),

        /// File too large.
        EFBIG(27),

        /// No space left on device.
        ENOSPC(28),

        /// Illegal seek.
        ESPIPE(29),

        /// Read-only file system.
        EROFS(30),

        /// Too many links.
        EMLINK(31),

        /// Broken pipe.
        EPIPE(32),

        /// math software.
        /// Numerical argument out of domain.
        EDOM(33),

        /// Result too large.
        ERANGE(34),

        /// non-blocking and interrupt i/o.
        /// Resource temporarily unavailable.
        EAGAIN(35),

        /// Operation would block.
        //public static var EWOULDBLOCK: POSIXErrorCode { get }

        /// Operation now in progress.
        EINPROGRESS(36),

        /// Operation already in progress.
        EALREADY(37),

        /// ipc/network software -- argument errors.
        /// Socket operation on non-socket.
        ENOTSOCK(38),

        /// Destination address required.
        EDESTADDRREQ(39),

        /// Message too long.
        EMSGSIZE(40),

        /// Protocol wrong type for socket.
        EPROTOTYPE(41),

        /// Protocol not available.
        ENOPROTOOPT(42),

        /// Protocol not supported.
        EPROTONOSUPPORT(43),

        /// Socket type not supported.
        ESOCKTNOSUPPORT(44),

        /// Operation not supported.
        ENOTSUP(45),

        /// Protocol family not supported.
        EPFNOSUPPORT(46),

        /// Address family not supported by protocol family.
        EAFNOSUPPORT(47),

        /// Address already in use.
        EADDRINUSE(48),

        /// Can't assign requested address.
        EADDRNOTAVAIL(49),

        /// ipc/network software -- operational errors
        /// Network is down.
        ENETDOWN(50),

        /// Network is unreachable.
        ENETUNREACH(51),

        /// Network dropped connection on reset.
        ENETRESET(52),

        /// Software caused connection abort.
        ECONNABORTED(53),

        /// Connection reset by peer.
        ECONNRESET(54),

        /// No buffer space available.
        ENOBUFS(55),

        /// Socket is already connected.
        EISCONN(56),

        /// Socket is not connected.
        ENOTCONN(57),

        /// Can't send after socket shutdown.
        ESHUTDOWN(58),

        /// Too many references: can't splice.
        ETOOMANYREFS(59),

        /// Operation timed out.
        ETIMEDOUT(60),

        /// Connection refused.
        ECONNREFUSED(61),

        /// Too many levels of symbolic links.
        ELOOP(62),

        /// File name too long.
        ENAMETOOLONG(63),

        /// Host is down.
        EHOSTDOWN(64),

        /// No route to host.
        EHOSTUNREACH(65),

        /// Directory not empty.
        ENOTEMPTY(66),

        /// quotas & mush.
        /// Too many processes.
        EPROCLIM(67),

        /// Too many users.
        EUSERS(68),

        /// Disc quota exceeded.
        EDQUOT(69),

        /// Network File System.
        /// Stale NFS file handle.
        ESTALE(70),

        /// Too many levels of remote in path.
        EREMOTE(71),

        /// RPC struct is bad.
        EBADRPC(72),

        /// RPC version wrong.
        ERPCMISMATCH(73),

        /// RPC prog. not avail.
        EPROGUNAVAIL(74),

        /// Program version wrong.
        EPROGMISMATCH(75),

        /// Bad procedure for program.
        EPROCUNAVAIL(76),

        /// No locks available.
        ENOLCK(77),

        /// Function not implemented.
        ENOSYS(78),

        /// Inappropriate file type or format.
        EFTYPE(79),

        /// Authentication error.
        EAUTH(80),

        /// Need authenticator.
        ENEEDAUTH(81),

        /// Intelligent device errors.
        /// Device power is off.
        EPWROFF(82),

        /// Device error, e.g. paper out.
        EDEVERR(83),

        /// Value too large to be stored in data type.
        EOVERFLOW(84),

        /// Bad executable.
        EBADEXEC(85),

        /// Bad CPU type in executable.
        EBADARCH(86),

        /// Shared library version mismatch.
        ESHLIBVERS(87),

        /// Malformed Macho file.
        EBADMACHO(88),

        /// Operation canceled.
        ECANCELED(89),

        /// Identifier removed.
        EIDRM(90),

        /// No message of desired type.
        ENOMSG(91),

        /// Illegal byte sequence.
        EILSEQ(92),

        /// Attribute not found.
        ENOATTR(93),

        /// Bad message.
        EBADMSG(94),

        /// Reserved.
        EMULTIHOP(95),

        /// No message available on STREAM.
        ENODATA(96),

        /// Reserved.
        ENOLINK(97),

        /// No STREAM resources.
        ENOSR(98),

        /// Not a STREAM.
        ENOSTR(99),

        /// Protocol error.
        EPROTO(100),

        /// STREAM ioctl timeout.
        ETIME(101),

        /// No such policy registered.
        ENOPOLICY(103),

        /// State not recoverable.
        ENOTRECOVERABLE(104),

        /// Previous owner died.
        EOWNERDEAD(105),

        /// Interface output queue is full.
        EQFULL(106);

        /// Must be equal largest errno.
        //public static var ELAST: POSIXErrorCode { get }

        companion object {
        }
    }

    fun Code(rawValue: Int): POSIXError.Code? {
        return when (rawValue) {
            1 -> Code.EPERM
            2 -> Code.ENOENT
            3 -> Code.ESRCH
            4 -> Code.EINTR
            5 -> Code.EIO
            6 -> Code.ENXIO
            7 -> Code.E2BIG
            8 -> Code.ENOEXEC
            9 -> Code.EBADF
            10 -> Code.ECHILD
            11 -> Code.EDEADLK
            12 -> Code.ENOMEM
            13 -> Code.EACCES
            14 -> Code.EFAULT
            15 -> Code.ENOTBLK
            16 -> Code.EBUSY
            17 -> Code.EEXIST
            18 -> Code.EXDEV
            19 -> Code.ENODEV
            20 -> Code.ENOTDIR
            21 -> Code.EISDIR
            22 -> Code.EINVAL
            23 -> Code.ENFILE
            24 -> Code.EMFILE
            25 -> Code.ENOTTY
            26 -> Code.ETXTBSY
            27 -> Code.EFBIG
            28 -> Code.ENOSPC
            29 -> Code.ESPIPE
            30 -> Code.EROFS
            31 -> Code.EMLINK
            32 -> Code.EPIPE
            33 -> Code.EDOM
            34 -> Code.ERANGE
            35 -> Code.EAGAIN
            36 -> Code.EINPROGRESS
            37 -> Code.EALREADY
            38 -> Code.ENOTSOCK
            39 -> Code.EDESTADDRREQ
            40 -> Code.EMSGSIZE
            41 -> Code.EPROTOTYPE
            42 -> Code.ENOPROTOOPT
            43 -> Code.EPROTONOSUPPORT
            44 -> Code.ESOCKTNOSUPPORT
            45 -> Code.ENOTSUP
            46 -> Code.EPFNOSUPPORT
            47 -> Code.EAFNOSUPPORT
            48 -> Code.EADDRINUSE
            49 -> Code.EADDRNOTAVAIL
            50 -> Code.ENETDOWN
            51 -> Code.ENETUNREACH
            52 -> Code.ENETRESET
            53 -> Code.ECONNABORTED
            54 -> Code.ECONNRESET
            55 -> Code.ENOBUFS
            56 -> Code.EISCONN
            57 -> Code.ENOTCONN
            58 -> Code.ESHUTDOWN
            59 -> Code.ETOOMANYREFS
            60 -> Code.ETIMEDOUT
            61 -> Code.ECONNREFUSED
            62 -> Code.ELOOP
            63 -> Code.ENAMETOOLONG
            64 -> Code.EHOSTDOWN
            65 -> Code.EHOSTUNREACH
            66 -> Code.ENOTEMPTY
            67 -> Code.EPROCLIM
            68 -> Code.EUSERS
            69 -> Code.EDQUOT
            70 -> Code.ESTALE
            71 -> Code.EREMOTE
            72 -> Code.EBADRPC
            73 -> Code.ERPCMISMATCH
            74 -> Code.EPROGUNAVAIL
            75 -> Code.EPROGMISMATCH
            76 -> Code.EPROCUNAVAIL
            77 -> Code.ENOLCK
            78 -> Code.ENOSYS
            79 -> Code.EFTYPE
            80 -> Code.EAUTH
            81 -> Code.ENEEDAUTH
            82 -> Code.EPWROFF
            83 -> Code.EDEVERR
            84 -> Code.EOVERFLOW
            85 -> Code.EBADEXEC
            86 -> Code.EBADARCH
            87 -> Code.ESHLIBVERS
            88 -> Code.EBADMACHO
            89 -> Code.ECANCELED
            90 -> Code.EIDRM
            91 -> Code.ENOMSG
            92 -> Code.EILSEQ
            93 -> Code.ENOATTR
            94 -> Code.EBADMSG
            95 -> Code.EMULTIHOP
            96 -> Code.ENODATA
            97 -> Code.ENOLINK
            98 -> Code.ENOSR
            99 -> Code.ENOSTR
            100 -> Code.EPROTO
            101 -> Code.ETIME
            103 -> Code.ENOPOLICY
            104 -> Code.ENOTRECOVERABLE
            105 -> Code.EOWNERDEAD
            106 -> Code.EQFULL
            else -> null
        }
    }




    /// IPC/Network software -- argument errors.




    /// IPC/Network software -- operational errors



    /// Quotas



    /// Network File System




    /// Intelligent device errors.



    /// Program loading errors.









    companion object {

        val _nsErrorDomain: String
            get() = NSPOSIXErrorDomain
        /// Operation not permitted.

        val EPERM: POSIXError.Code
            get() = POSIXError.Code.EPERM

        /// No such file or directory.
        val ENOENT: POSIXError.Code
            get() = POSIXError.Code.ENOENT

        /// No such process.
        val ESRCH: POSIXError.Code
            get() = POSIXError.Code.ESRCH

        /// Interrupted system call.
        val EINTR: POSIXError.Code
            get() = POSIXError.Code.EINTR

        /// Input/output error.
        val EIO: POSIXError.Code
            get() = POSIXError.Code.EIO

        /// Device not configured.
        val ENXIO: POSIXError.Code
            get() = POSIXError.Code.ENXIO

        /// Argument list too long.
        val E2BIG: POSIXError.Code
            get() = POSIXError.Code.E2BIG

        /// Exec format error.
        val ENOEXEC: POSIXError.Code
            get() = POSIXError.Code.ENOEXEC

        /// Bad file descriptor.
        val EBADF: POSIXError.Code
            get() = POSIXError.Code.EBADF

        /// No child processes.
        val ECHILD: POSIXError.Code
            get() = POSIXError.Code.ECHILD

        /// Resource deadlock avoided.
        val EDEADLK: POSIXError.Code
            get() = POSIXError.Code.EDEADLK

        /// Cannot allocate memory.
        val ENOMEM: POSIXError.Code
            get() = POSIXError.Code.ENOMEM

        /// Permission denied.
        val EACCES: POSIXError.Code
            get() = POSIXError.Code.EACCES

        /// Bad address.
        val EFAULT: POSIXError.Code
            get() = POSIXError.Code.EFAULT

        /// Device / Resource busy.
        val EBUSY: POSIXError.Code
            get() = POSIXError.Code.EBUSY

        /// File exists.
        val EEXIST: POSIXError.Code
            get() = POSIXError.Code.EEXIST

        /// Cross-device link.
        val EXDEV: POSIXError.Code
            get() = POSIXError.Code.EXDEV

        /// Operation not supported by device.
        val ENODEV: POSIXError.Code
            get() = POSIXError.Code.ENODEV

        /// Not a directory.
        val ENOTDIR: POSIXError.Code
            get() = POSIXError.Code.ENOTDIR

        /// Is a directory.
        val EISDIR: POSIXError.Code
            get() = POSIXError.Code.EISDIR

        /// Invalid argument.
        val EINVAL: POSIXError.Code
            get() = POSIXError.Code.EINVAL

        /// Too many open files in system.
        val ENFILE: POSIXError.Code
            get() = POSIXError.Code.ENFILE

        /// Too many open files.
        val EMFILE: POSIXError.Code
            get() = POSIXError.Code.EMFILE

        /// Inappropriate ioctl for device.
        val ENOTTY: POSIXError.Code
            get() = POSIXError.Code.ENOTTY

        /// File too large.
        val EFBIG: POSIXError.Code
            get() = POSIXError.Code.EFBIG

        /// No space left on device.
        val ENOSPC: POSIXError.Code
            get() = POSIXError.Code.ENOSPC

        /// Illegal seek.
        val ESPIPE: POSIXError.Code
            get() = POSIXError.Code.ESPIPE

        /// Read-only file system.
        val EROFS: POSIXError.Code
            get() = POSIXError.Code.EROFS

        /// Too many links.
        val EMLINK: POSIXError.Code
            get() = POSIXError.Code.EMLINK

        /// Broken pipe.
        val EPIPE: POSIXError.Code
            get() = POSIXError.Code.EPIPE

        /// Math Software

        /// Numerical argument out of domain.
        val EDOM: POSIXError.Code
            get() = POSIXError.Code.EDOM

        /// Result too large.
        val ERANGE: POSIXError.Code
            get() = POSIXError.Code.ERANGE

        /// Non-blocking and interrupt I/O.

        /// Resource temporarily unavailable.
        val EAGAIN: POSIXError.Code
            get() = POSIXError.Code.EAGAIN

        /// File name too long.
        val ENAMETOOLONG: POSIXError.Code
            get() = POSIXError.Code.ENAMETOOLONG

        /// Directory not empty.
        val ENOTEMPTY: POSIXError.Code
            get() = POSIXError.Code.ENOTEMPTY

        /// No locks available.
        val ENOLCK: POSIXError.Code
            get() = POSIXError.Code.ENOLCK

        /// Function not implemented.
        val ENOSYS: POSIXError.Code
            get() = POSIXError.Code.ENOSYS

        /// Operation canceled.
        val ECANCELED: POSIXError.Code
            get() = POSIXError.Code.ECANCELED

        /// Illegal byte sequence.
        val EILSEQ: POSIXError.Code
            get() = POSIXError.Code.EILSEQ
    }
}

internal sealed class UnknownNSError: Exception(), Error {
    class MissingErrorCase: UnknownNSError() {
        override fun equals(other: Any?): Boolean = other is MissingErrorCase
        override fun hashCode(): Int = "MissingErrorCase".hashCode()
    }

    companion object {
        val missingError: UnknownNSError
            get() = MissingErrorCase()
    }
}

// MARK: NSURLErrorDomain

/// `NSURLErrorDomain` indicates an `NSURL` error.
///
/// Constants used by `NSError` to differentiate between "domains" of error codes,
/// serving as a discriminator for error codes that originate from different subsystems or sources.
val NSURLErrorDomain: String = "NSURLErrorDomain"

/// The `NSError` userInfo dictionary key used to store and retrieve the URL which
/// caused a load to fail.
val NSURLErrorFailingURLErrorKey: String = "NSErrorFailingURLKey"

/// The `NSError` userInfo dictionary key used to store and retrieve the NSString
/// object for the URL which caused a load to fail.
val NSURLErrorFailingURLStringErrorKey: String = "NSErrorFailingURLStringKey"

/// The `NSError` userInfo dictionary key used to store and retrieve the
/// SecTrustRef object representing the state of a failed SSL handshake.
val NSURLErrorFailingURLPeerTrustErrorKey: String = "NSURLErrorFailingURLPeerTrustErrorKey"

/// The `NSError` userInfo dictionary key used to store and retrieve the
/// `NSNumber` corresponding to the reason why a background `URLSessionTask`
/// was cancelled
///
/// One of
/// * `NSURLErrorCancelledReasonUserForceQuitApplication`
/// * `NSURLErrorCancelledReasonBackgroundUpdatesDisabled`
/// * `NSURLErrorCancelledReasonInsufficientSystemResources`
val NSURLErrorBackgroundTaskCancelledReasonKey: String = "NSURLErrorBackgroundTaskCancelledReasonKey"

/// Code associated with `NSURLErrorBackgroundTaskCancelledReasonKey`
val NSURLErrorCancelledReasonUserForceQuitApplication: Int
    get() = 0
/// Code associated with `NSURLErrorBackgroundTaskCancelledReasonKey`
val NSURLErrorCancelledReasonBackgroundUpdatesDisabled: Int
    get() = 1
/// Code associated with `NSURLErrorBackgroundTaskCancelledReasonKey`
val NSURLErrorCancelledReasonInsufficientSystemResources: Int
    get() = 2

//MARK: NSURL-related Error Codes

val NSURLErrorUnknown: Int
    get() = -1
val NSURLErrorCancelled: Int
    get() = -999
val NSURLErrorBadURL: Int
    get() = -1000
val NSURLErrorTimedOut: Int
    get() = -1001
val NSURLErrorUnsupportedURL: Int
    get() = -1002
val NSURLErrorCannotFindHost: Int
    get() = -1003
val NSURLErrorCannotConnectToHost: Int
    get() = -1004
val NSURLErrorNetworkConnectionLost: Int
    get() = -1005
val NSURLErrorDNSLookupFailed: Int
    get() = -1006
val NSURLErrorHTTPTooManyRedirects: Int
    get() = -1007
val NSURLErrorResourceUnavailable: Int
    get() = -1008
val NSURLErrorNotConnectedToInternet: Int
    get() = -1009
val NSURLErrorRedirectToNonExistentLocation: Int
    get() = -1010
val NSURLErrorBadServerResponse: Int
    get() = -1011
val NSURLErrorUserCancelledAuthentication: Int
    get() = -1012
val NSURLErrorUserAuthenticationRequired: Int
    get() = -1013
val NSURLErrorZeroByteResource: Int
    get() = -1014
val NSURLErrorCannotDecodeRawData: Int
    get() = -1015
val NSURLErrorCannotDecodeContentData: Int
    get() = -1016
val NSURLErrorCannotParseResponse: Int
    get() = -1017
val NSURLErrorAppTransportSecurityRequiresSecureConnection: Int
    get() = -1022
val NSURLErrorFileDoesNotExist: Int
    get() = -1100
val NSURLErrorFileIsDirectory: Int
    get() = -1101
val NSURLErrorNoPermissionsToReadFile: Int
    get() = -1102
val NSURLErrorDataLengthExceedsMaximum: Int
    get() = -1103

// SSL errors
val NSURLErrorSecureConnectionFailed: Int
    get() = -1201
val NSURLErrorServerCertificateHasBadDate: Int
    get() = -1202
val NSURLErrorServerCertificateUntrusted: Int
    get() = -1203
val NSURLErrorServerCertificateHasUnknownRoot: Int
    get() = -1204
val NSURLErrorServerCertificateNotYetValid: Int
    get() = -1205
val NSURLErrorClientCertificateRejected: Int
    get() = -1206
val NSURLErrorClientCertificateRequired: Int
    get() = -1207
val NSURLErrorCannotLoadFromNetwork: Int
    get() = -2000

// Download and file I/O errors
val NSURLErrorCannotCreateFile: Int
    get() = -3000
val NSURLErrorCannotOpenFile: Int
    get() = -3001
val NSURLErrorCannotCloseFile: Int
    get() = -3002
val NSURLErrorCannotWriteToFile: Int
    get() = -3003
val NSURLErrorCannotRemoveFile: Int
    get() = -3004
val NSURLErrorCannotMoveFile: Int
    get() = -3005
val NSURLErrorDownloadDecodingFailedMidStream: Int
    get() = -3006
val NSURLErrorDownloadDecodingFailedToComplete: Int
    get() = -3007

val NSURLErrorInternationalRoamingOff: Int
    get() = -1018
val NSURLErrorCallIsActive: Int
    get() = -1019
val NSURLErrorDataNotAllowed: Int
    get() = -1020
val NSURLErrorRequestBodyStreamExhausted: Int
    get() = -1021

val NSURLErrorBackgroundSessionRequiresSharedContainer: Int
    get() = -995
val NSURLErrorBackgroundSessionInUseByAnotherProcess: Int
    get() = -996
val NSURLErrorBackgroundSessionWasDisconnected: Int
    get() = -997
