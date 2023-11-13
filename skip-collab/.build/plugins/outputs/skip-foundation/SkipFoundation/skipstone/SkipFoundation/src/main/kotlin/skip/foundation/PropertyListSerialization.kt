// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.

open class PropertyListSerialization {
    enum class PropertyListFormat {
        openStep,
        xml,
        binary;

        companion object {
        }
    }

    companion object {

        fun propertyList(from: Data, format: PropertyListSerialization.PropertyListFormat? = null): Dictionary<String, String>? {
            var dict: Dictionary<String, String> = dictionaryOf()
            val re = "\"(.*)\"[ ]*=[ ]*\"(.*)\";"
            //let re = #"(?<!\\)"(.*?)(?<!\\)"\s*=\s*"(.*?)(?<!\\)";"# // Swift Regex error: "lookbehind is not currently supported"

            val text = from.utf8String
            if (text == null) {
                // TODO: should this throw an error?
                return null
            }

            val exp: kotlin.text.Regex = re.toRegex()
            val matches = exp.findAll(text)
            for (match in matches.sref()) {
                if (match.groupValues.size == 3) {
                    match.groupValues[1].sref()?.let { key ->
                        match.groupValues[2].sref()?.let { value ->
                            dict[key.replacingOccurrences(of = "\\\\\\\"", with = "\"")] = value.replacingOccurrences(of = "\\\\\\\"", with = "\"")
                        }
                    }
                }
            }
            return dict.sref()
        }
    }
}
