// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

@Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
class AttributedString {

    companion object {
    }
}

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
// #if !SKIP
// import struct Foundation.AttributedString
// public typealias AttributedString = Foundation.AttributedString
// public typealias PlatformAttributedString = Foundation.AttributedString
// #else
// public typealias AttributedString = SkipAttributedString
// public typealias PlatformAttributedString = android.text.SpannableString
// #endif
//
//
// public struct SkipAttributedString {
//     public let platformValue: PlatformAttributedString
//
//     public init(platformValue: PlatformAttributedString) {
//         self.platformValue = platformValue
//     }
//
//     public init(_ platformValue: PlatformAttributedString = PlatformAttributedString()) {
//         self.platformValue = platformValue
//     }
// }
