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

open class HTTPURLResponse: URLResponse {
    internal constructor(url: URL, mimeType: String?, expectedContentLength: Int, textEncodingName: String?): super(url = url, mimeType = mimeType, expectedContentLength = expectedContentLength, textEncodingName = textEncodingName) {
    }

    var statusCode: Int = 0
        private set
    var allHeaderFields: Dictionary<String, String> = dictionaryOf()
        get() = field.sref({ this.allHeaderFields = it })
        private set(newValue) {
            field = newValue.sref()
        }
    private var httpVersion: String? = null

    constructor(url: URL, statusCode: Int, httpVersion: String?, headerFields: Dictionary<String, String>?): super(url = url, mimeType = null, expectedContentLength = -1, textEncodingName = null) {
        this.httpVersion = httpVersion
        this.allHeaderFields = headerFields ?: dictionaryOf()
        this.statusCode = statusCode

        this.expectedContentLength = value(forHTTPHeaderField = "Content-Length")?.toLongOrNull() ?: -1

        value(forHTTPHeaderField = "Content-Type")?.let { contentType ->
            // handle "text/HTML; charset=ISO-8859-4"
            val parts = contentType.split(separator = ';') // TODO: need to not split on semicolons within quoted strings, like a filename
            if (parts.count > 1) {
                this.mimeType = parts.firstOrNull()?.lowercased()
                for (part in parts.dropFirst()) {
                    val keyValue = part.split(separator = '=')
                    if (keyValue.firstOrNull()?.trim() == "charset") {
                        keyValue.lastOrNull()?.trim()?.let { charset ->
                            this.textEncodingName = charset.lowercased()
                        }
                    }
                }
            } else {
                this.mimeType = contentType.lowercased()
            }
        }
    }

    override val suggestedFilename: String?
        get() {
            fun splitStringWithQuotes(input: String, separator: String): Array<String> {
                val regex = Regex("(?<=^|\\s|\\w)(?<!\\w)${separator}(?=\\s|\\w)")
                return Array(regex.split(input))
            }

            // let f = ["Content-Disposition": "attachment; filename=\"fname.ext\""]
            value(forHTTPHeaderField = "Content-Disposition")?.let { contentDisposition ->
                var contentDisposition = contentDisposition
                if (contentDisposition.hasPrefix("attachment;")) {
                    //let parts = splitStringWithQuotes(input: contentDisposition, separator: ";")
                    val parts = contentDisposition.split(separator = ';').map({ it -> it.trimmingCharacters(in_ = CharacterSet.whitespacesAndNewlines) })

                    if (parts.firstOrNull() == "attachment") {
                        for (part in parts.dropFirst()) {
                            val keyValue = part.split(separator = '=')
                            if (keyValue.firstOrNull()?.trim() == "filename") {
                                var filename_0 = keyValue.lastOrNull()?.trim(*"\"".toCharArray())
                                if (filename_0 == null) {
                                    continue
                                }
                                filename_0 = filename_0.replace("/", "_") // escape path separators
                                return filename_0
                            }
                        }
                    }
                }
            }
            return super.suggestedFilename // fallback to super impl
        }

    open fun value(forHTTPHeaderField: String): String? {
        val field = forHTTPHeaderField
        return URLRequest.value(forHTTPHeaderField = field, in_ = allHeaderFields)
    }

    companion object {

        fun localizedString(forStatusCode: Int): String {
            val statusCode = forStatusCode
            when (statusCode) {
                100 -> return "Continue"
                101 -> return "Switching Protocols"
                102 -> return "Processing"
                200 -> return "OK"
                201 -> return "Created"
                202 -> return "Accepted"
                203 -> return "Non-Authoritative Information"
                204 -> return "No Content"
                205 -> return "Reset Content"
                206 -> return "Partial Content"
                207 -> return "Multi-Status"
                208 -> return "Already Reported"
                226 -> return "IM Used"
                300 -> return "Multiple Choices"
                301 -> return "Moved Permanently"
                302 -> return "Found"
                303 -> return "See Other"
                304 -> return "Not Modified"
                305 -> return "Use Proxy"
                307 -> return "Temporary Redirect"
                308 -> return "Permanent Redirect"
                400 -> return "Bad Request"
                401 -> return "Unauthorized"
                402 -> return "Payment Required"
                403 -> return "Forbidden"
                404 -> return "Not Found"
                405 -> return "Method Not Allowed"
                406 -> return "Not Acceptable"
                407 -> return "Proxy Authentication Required"
                408 -> return "Request Timeout"
                409 -> return "Conflict"
                410 -> return "Gone"
                411 -> return "Length Required"
                412 -> return "Precondition Failed"
                413 -> return "Payload Too Large"
                414 -> return "URI Too Long"
                415 -> return "Unsupported Media Type"
                416 -> return "Range Not Satisfiable"
                417 -> return "Expectation Failed"
                418 -> return "I'm a teapot"
                421 -> return "Misdirected Request"
                422 -> return "Unprocessable Entity"
                423 -> return "Locked"
                424 -> return "Failed Dependency"
                426 -> return "Upgrade Required"
                428 -> return "Precondition Required"
                429 -> return "Too Many Requests"
                431 -> return "Request Header Fields Too Large"
                451 -> return "Unavailable For Legal Reasons"
                500 -> return "Internal Server Error"
                501 -> return "Not Implemented"
                502 -> return "Bad Gateway"
                503 -> return "Service Unavailable"
                504 -> return "Gateway Timeout"
                505 -> return "HTTP Version Not Supported"
                506 -> return "Variant Also Negotiates"
                507 -> return "Insufficient Storage"
                508 -> return "Loop Detected"
                510 -> return "Not Extended"
                511 -> return "Network Authentication Required"
                else -> return "Unknown"
            }
        }
    }
}
