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
typealias PlatformBundle = AnyClass

// TODO: each platform should have a local Bundle.module extension created. static extensions not yet supported

open class Bundle {

    private val location: SkipLocalizedStringResource.BundleDescription

    constructor(location: SkipLocalizedStringResource.BundleDescription) {
        this.location = location
    }

    constructor(path: String): this(location = SkipLocalizedStringResource.BundleDescription.atURL(URL(fileURLWithPath = path))) {
    }

    constructor(url: URL): this(location = SkipLocalizedStringResource.BundleDescription.atURL(url)) {
    }

    constructor(for_: AnyClass): this(location = SkipLocalizedStringResource.BundleDescription.forClass(for_)) {
    }

    open val description: String
        get() = location.description

    open val bundleURL: URL
        get() {
            val loc: SkipLocalizedStringResource.BundleDescription = location
            when (loc) {
                is SkipLocalizedStringResource.BundleDescription.MainCase -> {
                    fatalError("Skip does not support .main bundle")
                }
                is SkipLocalizedStringResource.BundleDescription.AtURLCase -> {
                    val url = loc.associated0
                    return url
                }
                is SkipLocalizedStringResource.BundleDescription.ForClassCase -> {
                    val cls = loc.associated0
                    return relativeBundleURL("resources.lst")!!
                        .deletingLastPathComponent()
                }
            }
        }

    open val resourceURL: URL?
        get() = bundleURL // FIXME: this is probably not correct


    /// Creates a relative path to the given bundle URL
    private fun relativeBundleURL(path: String): URL? {
        val loc: SkipLocalizedStringResource.BundleDescription = location
        when (loc) {
            is SkipLocalizedStringResource.BundleDescription.MainCase -> {
                fatalError("Skip does not support .main bundle")
            }
            is SkipLocalizedStringResource.BundleDescription.AtURLCase -> {
                val url = loc.associated0
                return url.appendingPathComponent(path)
            }
            is SkipLocalizedStringResource.BundleDescription.ForClassCase -> {
                val cls = loc.associated0
                try {
                    val resURL = cls.java.getResource("Resources/" + path)
                    return URL(platformValue = resURL)
                } catch (error: Throwable) {
                    @Suppress("NAME_SHADOWING") val error = error.aserror()
                    // getResource throws when it cannot find the resource
                    return null
                }
            }
        }
    }

    open val bundlePath: String
        get() = bundleURL.path

    /// Loads the resources index stored in the `resources.lst` file at the root of the resources folder.
    private fun loadResourcePaths(): Array<String> {
        val resourceListURL_0 = url(forResource = "resources.lst")
        if (resourceListURL_0 == null) {
            return arrayOf()
        }
        val resourceList = Data(contentsOf = resourceListURL_0)
        val resourceListString_0 = String(data = resourceList, encoding = StringEncoding.utf8)
        if (resourceListString_0 == null) {
            return arrayOf()
        }
        val resourcePaths = resourceListString_0.components(separatedBy = "\n")
        return resourcePaths.sref()
    }

    open val localizations: Array<String>
        get() {
            return loadResourcePaths()
                .compactMap({ it -> it.components(separatedBy = "/").first })
                .filter({ it -> it.hasSuffix(".lproj") })
                .map({ it -> it.dropLast(".lproj".count) })
        }

    open fun path(forResource: String? = null, ofType: String? = null, inDirectory: String? = null, forLocalization: String? = null): String? = url(forResource = forResource, withExtension = ofType, subdirectory = inDirectory, localization = forLocalization)?.path

    open fun url(forResource: String? = null, withExtension: String? = null, subdirectory: String? = null, localization: String? = null): URL? {
        // similar behavior to: https://github.com/apple/swift-corelibs-foundation/blob/69ab3975ea636d1322ad19bbcea38ce78b65b26a/CoreFoundation/PlugIn.subproj/CFBundle_Resources.c#L1114
        var res = forResource ?: ""
        if ((withExtension != null) && !withExtension.isEmpty) {
            // TODO: If `forResource` is nil, we are expected to find the first file in the bundle whose extension matches
            res += "." + withExtension
        } else if (res.isEmpty) {
            return null
        }
        if (localization != null) {
            //let lprojExtension = "lproj" // _CFBundleLprojExtension
            var lprojExtensionWithDot = ".lproj" // _CFBundleLprojExtensionWithDot
            res = localization + lprojExtensionWithDot + "/" + res
        }
        if (subdirectory != null) {
            res = subdirectory + "/" + res
        }

        return relativeBundleURL(path = res)
    }

    open fun localizedString(forKey: String, value: String?, table: String?): String {
        val key = forKey
        val tableName = table
        val table = tableName ?: "Localizable"
        return key // TODO: load localization
    }

    companion object {
        val main = Bundle(location = SkipLocalizedStringResource.BundleDescription.main)
        // FIXME: this is terribly expensive, since it generates a stack trace and dyamically loads the class;
        // once static extensions are supported, this can be rectified with locally-generated modules.

        val module: Bundle
            get() {
                var callingClassName = Thread.currentThread().stackTrace[2].className.sref()
                // work-around the issue where Kotlin's top-level functions are compiled as being part of a synthesized FileNameKt file
                if (callingClassName.hasSuffix("Kt")) {
                    callingClassName = callingClassName.dropLast(2)
                }
                val callingClass = Class.forName(callingClassName)
                return Bundle(callingClass.kotlin as AnyClass)
            }
    }
}


fun NSLocalizedString(key: String, tableName: String? = null, bundle: Bundle = Bundle.main, value: String = "", comment: String): String {
    // TODO: access the java.util.ResourceBundle for the calling class
    return key
}


internal val _SkipFoundationBundle = Bundle(for_ = Bundle::class as kotlin.reflect.KClass<Any>)
//let _SkipFoundationBundle = Bundle.module
