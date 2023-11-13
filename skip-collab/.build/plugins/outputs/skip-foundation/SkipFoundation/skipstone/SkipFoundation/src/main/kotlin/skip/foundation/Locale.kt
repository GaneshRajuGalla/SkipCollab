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
internal typealias PlatformLocale = java.util.Locale
internal typealias NSLocale = Locale

class Locale {
    internal val platformValue: java.util.Locale

    internal constructor(platformValue: java.util.Locale) {
        this.platformValue = platformValue
    }

    constructor(identifier: String) {
        //self.platformValue = PlatformLocale(identifier)
        //self.platformValue = PlatformLocale.forLanguageTag(identifier)
        val parts = Array(identifier.split(separator = '_'))
        if (parts.count >= 2) {
            // turn fr_FR into the language/country form
            this.platformValue = java.util.Locale(parts.first, parts.last)
        } else {
            // language only
            this.platformValue = java.util.Locale(identifier)
        }
    }

    val identifier: String
        get() = platformValue.toString()

    val languageCode: String?
        get() = platformValue.getLanguage()

    fun localizedString(forLanguageCode: String): String? {
        val languageCode = forLanguageCode
        return java.util.Locale(languageCode).getDisplayLanguage(platformValue)
    }

    val currencySymbol: String?
        get() = java.text.NumberFormat.getCurrencyInstance(platformValue).currency?.symbol

    fun kotlin(nocopy: Boolean = false): java.util.Locale = if (nocopy) platformValue else platformValue.clone() as java.util.Locale

    override fun equals(other: Any?): Boolean {
        if (other !is Locale) return false
        return platformValue == other.platformValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object {

        val current: Locale
            get() = Locale(platformValue = java.util.Locale.getDefault())

        val system: Locale
            get() = Locale(platformValue = java.util.Locale.getDefault()) // FIXME: not the same as .system: “Use the system locale when you don’t want any localizations”
    }
}


internal fun java.util.Locale.swift(nocopy: Boolean = false): Locale {
    val platformValue = if (nocopy) this else clone() as java.util.Locale
    return Locale(platformValue = platformValue)
}
