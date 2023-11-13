// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class Font: Sendable {
    internal val fontImpl: @Composable () -> androidx.compose.ui.text.TextStyle

    internal constructor(fontImpl: @Composable () -> androidx.compose.ui.text.TextStyle) {
        this.fontImpl = fontImpl
    }

    enum class TextStyle: CaseIterable, Sendable {
        largeTitle,
        title,
        title2,
        title3,
        headline,
        subheadline,
        body,
        callout,
        footnote,
        caption,
        caption2;

        companion object {
            val allCases: Array<Font.TextStyle>
                get() = arrayOf(largeTitle, title, title2, title3, headline, subheadline, body, callout, footnote, caption, caption2)
        }
    }

    fun italic(): Font {
        return Font(fontImpl = { fontImpl().copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic) })
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun smallCaps(): Font {
        fatalError()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun lowercaseSmallCaps(): Font {
        fatalError()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun uppercaseSmallCaps(): Font {
        fatalError()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun monospacedDigit(): Font {
        fatalError()
    }

    fun weight(weight: Font.Weight): Font {
        return Font(fontImpl = l@{
            when (weight) {
                Font.Weight.ultraLight -> return@l fontImpl().copy(fontWeight = FontWeight.Thin)
                Font.Weight.thin -> return@l fontImpl().copy(fontWeight = FontWeight.ExtraLight)
                Font.Weight.light -> return@l fontImpl().copy(fontWeight = FontWeight.Light)
                Font.Weight.regular -> return@l fontImpl().copy(fontWeight = FontWeight.Normal)
                Font.Weight.medium -> return@l fontImpl().copy(fontWeight = FontWeight.Medium)
                Font.Weight.semibold -> return@l fontImpl().copy(fontWeight = FontWeight.SemiBold)
                Font.Weight.bold -> return@l fontImpl().copy(fontWeight = FontWeight.Bold)
                Font.Weight.heavy -> return@l fontImpl().copy(fontWeight = FontWeight.ExtraBold)
                Font.Weight.black -> return@l fontImpl().copy(fontWeight = FontWeight.Black)
                else -> return@l fontImpl().copy(fontWeight = FontWeight.Normal)
            }
        })
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun width(width: Font.Width): Font {
        fatalError()
    }

    fun bold(): Font = weight(Font.Weight.bold)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun monospaced(): Font {
        fatalError()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun leading(leading: Font.Leading): Font {
        fatalError()
    }

    class Weight: Sendable {
        internal val value: Double

        internal constructor(value: Double) {
            this.value = value
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Font.Weight) return false
            return value == other.value
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, value)
            return result
        }

        companion object {
            val ultraLight = Weight(value = -0.8)
            val thin = Weight(value = -0.6)
            val light = Weight(value = -0.4)
            val regular = Weight(value = 0.0)
            val medium = Weight(value = 0.23)
            val semibold = Weight(value = 0.3)
            val bold = Weight(value = 0.4)
            val heavy = Weight(value = 0.56)
            val black = Weight(value = 0.62)
        }
    }

    class Width: Sendable, MutableStruct {
        var value: Double
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(value: Double) {
            this.value = value
        }

        private constructor(copy: MutableStruct) {
            @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as Font.Width
            this.value = copy.value
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = Font.Width(this as MutableStruct)

        override fun equals(other: Any?): Boolean {
            if (other !is Font.Width) return false
            return value == other.value
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, value)
            return result
        }

        companion object {

            // TODO: Real values
            val compressed = Width(0.8)
            val condensed = Width(0.9)
            val standard = Width(1.0)
            val expanded = Width(1.2)
        }
    }

    enum class Leading: Sendable {
        standard,
        tight,
        loose;

        companion object {
        }
    }

    enum class Design: Sendable {
        default,
        serif,
        rounded,
        monospaced;

        companion object {
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(font: Any) {
        fontImpl = { MaterialTheme.typography.bodyMedium }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Font) return false
        return fontImpl == other.fontImpl
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, fontImpl)
        return result
    }

    companion object {
        // M3: Default Font Size/Line Height
        // displayLarge: Roboto 57/64
        // displayMedium: Roboto 45/52
        // displaySmall: Roboto 36/44
        // headlineLarge: Roboto 32/40
        // headlineMedium: Roboto 28/36
        // headlineSmall: Roboto 24/32
        // titleLarge: New-Roboto Medium 22/28
        // titleMedium: Roboto Medium 16/24
        // titleSmall: Roboto Medium 14/20
        // bodyLarge: Roboto 16/24
        // bodyMedium: Roboto 14/20
        // bodySmall: Roboto 12/16
        // labelLarge: Roboto Medium 14/20
        // labelMedium: Roboto Medium 12/16
        // labelSmall: New Roboto Medium 11/16

        // manual offsets are applied to the default font sizes to get them to line up with SwiftUI default sizes; see TextTests.swift


        val largeTitle = Font(fontImpl = { adjust(MaterialTheme.typography.titleLarge, by = Float(+9.0 + 1.0)) })

        val title = Font(fontImpl = { adjust(MaterialTheme.typography.headlineMedium, by = Float(-2.0)) })

        val title2 = Font(fontImpl = { adjust(MaterialTheme.typography.headlineSmall, by = Float(-5.0 + 1.0)) })

        val title3 = Font(fontImpl = { adjust(MaterialTheme.typography.headlineSmall, by = Float(-6.0)) })

        val headline = Font(fontImpl = { adjust(MaterialTheme.typography.titleMedium, by = 0.0f) })

        val subheadline = Font(fontImpl = { adjust(MaterialTheme.typography.titleSmall, by = 0.0f) })

        val body = Font(fontImpl = { adjust(MaterialTheme.typography.bodyLarge, by = 0.0f) })

        val callout = Font(fontImpl = { adjust(MaterialTheme.typography.bodyMedium, by = Float(+1.0)) })

        val footnote = Font(fontImpl = { adjust(MaterialTheme.typography.bodySmall, by = Float(+0.0)) })

        val caption = Font(fontImpl = { adjust(MaterialTheme.typography.bodySmall, by = Float(-0.75)) })

        val caption2 = Font(fontImpl = { adjust(MaterialTheme.typography.bodySmall, by = Float(-1.0)) })

        private fun adjust(style: androidx.compose.ui.text.TextStyle, by: Float): androidx.compose.ui.text.TextStyle {
            val amount = by
            return (if (amount == 0.0f) style else style.copy(fontSize = (style.fontSize.value + amount).sp)).sref()
        }

        fun system(style: Font.TextStyle): Font {
            when (style) {
                Font.TextStyle.largeTitle -> return Font.largeTitle
                Font.TextStyle.title -> return Font.title
                Font.TextStyle.title2 -> return Font.title2
                Font.TextStyle.title3 -> return Font.title3
                Font.TextStyle.headline -> return Font.headline
                Font.TextStyle.subheadline -> return Font.subheadline
                Font.TextStyle.body -> return Font.body
                Font.TextStyle.callout -> return Font.callout
                Font.TextStyle.footnote -> return Font.footnote
                Font.TextStyle.caption -> return Font.caption
                Font.TextStyle.caption2 -> return Font.caption2
            }
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun system(style: Font.TextStyle, design: Font.Design?): Font {
            fatalError()
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun system(style: Font.TextStyle, design: Font.Design? = null, weight: Font.Weight?): Font {
            fatalError()
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun system(size: Double, weight: Font.Weight? = null, design: Font.Design? = null): Font {
            fatalError()
        }

        fun custom(name: String, size: Double): Font {
            return Font(fontImpl = {
                // note that Android can find "courier" but not "Courier"
                androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily(android.graphics.Typeface.create(name, android.graphics.Typeface.NORMAL)), fontSize = androidx.compose.ui.unit.TextUnit(Float(size), androidx.compose.ui.unit.TextUnitType.Sp))
            })
        }

        fun custom(name: String, size: Double, relativeTo: Font.TextStyle): Font {
            val textStyle = relativeTo
            // TODO: handle textStyle
            return Font.custom(name, size = size)
        }

        fun custom(name: String, fixedSize: Double, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null): Font {
            // TODO: handle fixed size (somehow)
            return Font.custom(name, size = fixedSize)
        }
    }
}

enum class LegibilityWeight: Sendable {
    regular,
    bold;

    companion object {
    }
}

class RedactionReasons: OptionSet<RedactionReasons, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): RedactionReasons = RedactionReasons(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: RedactionReasons): Unit = assignfrom(target)

    private fun assignfrom(target: RedactionReasons) {
        this.rawValue = target.rawValue
    }

    companion object {

        val placeholder = RedactionReasons(rawValue = 1 shl 0)
        val privacy = RedactionReasons(rawValue = 1 shl 1)
        val invalidated = RedactionReasons(rawValue = 1 shl 2)

        fun of(vararg options: RedactionReasons): RedactionReasons {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return RedactionReasons(rawValue = value)
        }
    }
}

