// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias LocalizedStringResource = SkipLocalizedStringResource

// Override the Kotlin type to be public while keeping the Swift version internal:
class SkipLocalizedStringResource {
    val key: String
    val defaultValue: String? // TODO: String.LocalizationValue
    val table: String?
    var locale: Locale? = null
    var bundle: SkipLocalizedStringResource.BundleDescription? = null
    var comment: String? = null

    constructor(key: String, defaultValue: String? = null, table: String? = null, locale: Locale? = null, bundle: SkipLocalizedStringResource.BundleDescription? = null, comment: String? = null) {
        this.key = key
        this.defaultValue = defaultValue
        this.table = table
        this.locale = locale
        this.bundle = bundle
        this.comment = comment
    }

    // FIXME: move to `Bundle.BundleDescription` so we can internalize the location
    sealed class BundleDescription {
        class MainCase: BundleDescription() {
        }
        class ForClassCase(val associated0: AnyClass): BundleDescription() {
        }
        class AtURLCase(val associated0: URL): BundleDescription() {
        }

        val description: String
            get() {
                when (this) {
                    is SkipLocalizedStringResource.BundleDescription.MainCase -> return "bundle: main"
                    is SkipLocalizedStringResource.BundleDescription.ForClassCase -> {
                        val c = this.associated0
                        return "bundle: ${c}"
                    }
                    is SkipLocalizedStringResource.BundleDescription.AtURLCase -> {
                        val url = this.associated0
                        return "bundle: ${url}"
                    }
                }
            }

        override fun toString(): String = description

        companion object {
            val main: BundleDescription = MainCase()
            fun forClass(associated0: AnyClass): BundleDescription = ForClassCase(associated0)
            fun atURL(associated0: URL): BundleDescription = AtURLCase(associated0)
        }
    }
}


fun String(localized: SkipLocalizedStringResource): String {
    return fatalError("TODO: String(localized:)")
}


