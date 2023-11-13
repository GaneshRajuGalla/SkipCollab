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
typealias PlatformURL = java.net.URL
typealias NSURL = URL

class URL: Codable, MutableStruct {
    internal var platformValue: java.net.URL
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    private var isDirectoryFlag: Boolean?
    var baseURL: URL?

    constructor(platformValue: java.net.URL, isDirectory: Boolean? = null, baseURL: URL? = null) {
        this.platformValue = platformValue
        this.isDirectoryFlag = isDirectory
        this.baseURL = baseURL.sref()
    }

    constructor(url: URL) {
        this.platformValue = url.platformValue
        this.isDirectoryFlag = url.isDirectoryFlag
        this.baseURL = url.baseURL.sref()
    }

    constructor(fileURLWithPath: String, isDirectory: Boolean? = null, relativeTo: URL? = null) {
        val path = fileURLWithPath
        val base = relativeTo
        this.platformValue = java.net.URL("file://" + path) // TODO: escaping
        this.baseURL = base.sref() // TODO: base resolution
        this.isDirectoryFlag = isDirectory ?: path.hasSuffix("/") // TODO: should we hit the file system like NSURL does?
    }

    constructor(from: Decoder) {
        val decoder = from
        val container = decoder.singleValueContainer()
        val assignfrom = URL(string = container.decode(String::class))!!
        this.platformValue = assignfrom.platformValue
        this.isDirectoryFlag = assignfrom.isDirectoryFlag
        this.baseURL = assignfrom.baseURL
    }

    override fun encode(to: Encoder) {
        val encoder = to
        val container = encoder.singleValueContainer()
        container.encode(absoluteString)
    }

    val description: String
        get() = platformValue.description

    /// Converts this URL to a `java.nio.file.Path`.
    fun toPath(): java.nio.file.Path = java.nio.file.Paths.get(platformValue.toURI())


    val host: String?
        get() = platformValue.host

    val hasDirectoryPath: Boolean
        get() = this.isDirectoryFlag == true

    val path: String
        get() = platformValue.path

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val port: Int?
        get() {
            return fatalError("TODO: implement port")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val scheme: String?
        get() {
            return fatalError("TODO: implement scheme")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val query: String?
        get() {
            return fatalError("TODO: implement query")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val user: String?
        get() {
            return fatalError("TODO: implement user")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val password: String?
        get() {
            return fatalError("TODO: implement password")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val fragment: String?
        get() {
            return fatalError("TODO: implement fragment")
        }

    val standardized: URL
        get() = URL(platformValue = toPath().normalize().toUri().toURL())

    val absoluteString: String
        get() = platformValue.toExternalForm()

    val lastPathComponent: String
        get() = pathComponents.lastOrNull() ?: ""

    val pathExtension: String
        get() {
            val parts = Array((lastPathComponent ?: "").split(separator = '.'))
            if (parts.count >= 2) {
                return parts.last!!
            } else {
                return ""
            }
        }

    val isFileURL: Boolean
        get() = platformValue.protocol == "file"

    val pathComponents: Array<String>
        get() {
            val path: String = platformValue.path
            return Array(path.split(separator = '/')).filter { it -> !it.isEmpty }
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val relativePath: String
        get() {
            return fatalError("TODO: implement relativePath")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val relativeString: String
        get() {
            return fatalError("TODO: implement relativeString")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val standardizedFileURL: URL
        get() {
            return fatalError("TODO: implement standardizedFileURL")
        }

    fun standardize() {
        willmutate()
        try {
            assignfrom(standardized)
        } finally {
            didmutate()
        }
    }

    val absoluteURL: URL
        get() = this

    fun appendingPathComponent(pathComponent: String): URL {
        var url = this.platformValue.toExternalForm()
        if (!url.hasSuffix("/")) {
            url = url + "/"
        }
        url = url + pathComponent
        return URL(platformValue = java.net.URL(url))
    }

    fun appendingPathComponent(pathComponent: String, isDirectory: Boolean): URL {
        var url = this.platformValue.toExternalForm()
        if (!url.hasSuffix("/")) {
            url = url + "/"
        }
        url = url + pathComponent
        return URL(platformValue = java.net.URL(url), isDirectory = isDirectory)
    }

    fun deletingLastPathComponent(): URL {
        var url = this.platformValue.toExternalForm()
        while (url.hasSuffix("/") && !url.isEmpty) {
            url = url.dropLast(1)
        }
        while (!url.hasSuffix("/") && !url.isEmpty) {
            url = url.dropLast(1)
        }
        return URL(platformValue = java.net.URL(url))
    }

    fun appendingPathExtension(pathExtension: String): URL {
        var url = this.platformValue.toExternalForm()
        url = url + "." + pathExtension
        return URL(platformValue = java.net.URL(url))
    }

    fun deletingPathExtension(): URL {
        val ext = pathExtension
        var url = this.platformValue.toExternalForm()
        while (url.hasSuffix("/")) {
            url = url.dropLast(1)
        }
        if (url.hasSuffix("." + ext)) {
            url = url.dropLast(ext.count + 1)
        }
        return URL(platformValue = java.net.URL(url))
    }

    fun resolvingSymlinksInPath(): URL {
        if (isFileURL == false) {
            return this.sref()
        }
        val originalPath = toPath()
        //if !java.nio.file.Files.isSymbolicLink(originalPath) {
        //    return self // not a link
        //} else {
        //    let normalized = java.nio.file.Files.readSymbolicLink(originalPath).normalize()
        //    return URL(platformValue: normalized.toUri().toURL())
        //}
        try {
            return URL(platformValue = originalPath.toRealPath().toUri().toURL())
        } catch (error: Throwable) {
            @Suppress("NAME_SHADOWING") val error = error.aserror()
            // this will fail if the file does not exist, but Foundation expects it to return the path itself
            return this.sref()
        }
    }

    fun resolveSymlinksInPath() {
        willmutate()
        try {
            assignfrom(resolvingSymlinksInPath())
        } finally {
            didmutate()
        }
    }

    fun checkResourceIsReachable(): Boolean {
        if (!isFileURL) {
            // “This method is currently applicable only to URLs for file system resources. For other URL types, `false` is returned.”
            return false
        }
        // check whether the resource can be reached by opening and closing a connection
        platformValue.openConnection().getInputStream().close()
        return true
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun removeAllCachedResourceValues() {
        willmutate()
        try {
            fatalError("TODO: implement removeAllCachedResourceValues")
        } finally {
            didmutate()
        }
    }


    fun kotlin(nocopy: Boolean = false): java.net.URL = platformValue

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as URL
        this.platformValue = copy.platformValue
        this.isDirectoryFlag = copy.isDirectoryFlag
        this.baseURL = copy.baseURL
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = URL(this as MutableStruct)

    private fun assignfrom(target: URL) {
        this.platformValue = target.platformValue
        this.isDirectoryFlag = target.isDirectoryFlag
        this.baseURL = target.baseURL
    }

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is URL) return false
        return platformValue == other.platformValue && isDirectoryFlag == other.isDirectoryFlag && baseURL == other.baseURL
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        result = Hasher.combine(result, isDirectoryFlag)
        result = Hasher.combine(result, baseURL)
        return result
    }

    companion object: DecodableCompanion<URL> {
        override fun init(from: Decoder): URL = URL(from = from)
    }
}


// MARK: Optional Constructors

class URLResourceKey: RawRepresentable<String> {
    override val rawValue: String

    constructor(rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.rawValue = rawValue
    }

    constructor(rawValue: String) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is URLResourceKey) return false
        return rawValue == other.rawValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, rawValue)
        return result
    }

    companion object {
    }
}

class URLResourceValues: MutableStruct {
    var allValues: Dictionary<URLResourceKey, Any>
        get() = field.sref({ this.allValues = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(allValues: Dictionary<URLResourceKey, Any>) {
        this.allValues = allValues
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = URLResourceValues(allValues)

    companion object {
    }
}

fun URL(string: String, relativeTo: URL? = null): URL? {
    val baseURL = relativeTo
    try {
        val url = java.net.URL(relativeTo?.platformValue, string) // throws on malformed
        // use the same logic as the constructor so that `URL(fileURLWithPath: "/tmp/") == URL(string: "file:///tmp/")`
        val isDirectory = url.protocol == "file" && string.hasSuffix("/")
        return URL(url, isDirectory = isDirectory, baseURL = baseURL)
    } catch (error: Throwable) {
        @Suppress("NAME_SHADOWING") val error = error.aserror()
        // e.g., malformed URL
        return null
    }
}

//public func URL(fileURLWithPath path: String, relativeTo: URL? = nil, isDirectory: Bool? = nil) -> URL {
//    URL(fileURLWithPath: path, relativeTo: relativeTo, isDirectory: isDirectory)
//}

//public func URL(fileURLWithFileSystemRepresentation path: String, relativeTo: URL?, isDirectory: Bool) -> URL {
//    // SKIP INSERT: val nil = null
//    if (relativeTo != nil) {
//        return URL(platformValue: PlatformURL(relativeTo?.platformValue, path))
//    } else {
//        return URL(platformValue: PlatformURL("file://" + path)) // TODO: isDirectory handling?
//    }
//}



internal fun java.net.URL.swift(nocopy: Boolean = false): URL = URL(platformValue = this)
