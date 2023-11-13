// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Color: View, Sendable, ShapeStyle {
    internal val colorImpl: @Composable () -> androidx.compose.ui.graphics.Color

    internal constructor(colorImpl: @Composable () -> androidx.compose.ui.graphics.Color) {
        this.colorImpl = colorImpl
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val modifier = context.modifier.background(colorImpl()).fillSize(expandContainer = false)
        Box(modifier = modifier)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(color: Any, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        colorImpl = { androidx.compose.ui.graphics.Color.White }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(cgColor: Any, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null, @Suppress("UNUSED_PARAMETER") unusedp_1: Nothing? = null) {
        colorImpl = { androidx.compose.ui.graphics.Color.White }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val cgColor: Any?
        get() {
            fatalError()
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun resolve(in_: Any): Color.Resolved {
        val environment = in_
        fatalError()
    }

    // MARK: -

    enum class RGBColorSpace: Sendable {
        sRGB,
        sRGBLinear,
        displayP3;

        companion object {
        }
    }

    constructor(red: Double, green: Double, blue: Double, opacity: Double = 1.0) {
        colorImpl = { androidx.compose.ui.graphics.Color(red = Float(red), green = Float(green), blue = Float(blue), alpha = Float(opacity)) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(colorSpace: Color.RGBColorSpace, red: Double, green: Double, blue: Double, opacity: Double = 1.0): this(red = red, green = green, blue = blue, opacity = opacity) {
    }

    constructor(white: Double, opacity: Double = 1.0): this(red = white, green = white, blue = white, opacity = opacity) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(colorSpace: Color.RGBColorSpace, white: Double, opacity: Double = 1.0): this(white = white, opacity = opacity) {
    }

    constructor(hue: Double, saturation: Double, brightness: Double, opacity: Double = 1.0, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        colorImpl = { androidx.compose.ui.graphics.Color.hsl(hue = Float(hue), saturation = Float(saturation), lightness = Float(brightness), alpha = Float(opacity)) }
    }

    // MARK: -

    class Resolved: MutableStruct {
        var red: Float
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }
        var green: Float
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }
        var blue: Float
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }
        var opacity: Float
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(red: Float, green: Float, blue: Float, opacity: Float) {
            this.red = red
            this.green = green
            this.blue = blue
            this.opacity = opacity
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        constructor(colorSpace: Color.RGBColorSpace, red: Float, green: Float, blue: Float, opacity: Float): this(red = red, green = green, blue = blue, opacity = opacity) {
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val cgColor: Any
            get() {
                fatalError()
            }

        private constructor(copy: MutableStruct) {
            @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as Color.Resolved
            this.red = copy.red
            this.green = copy.green
            this.blue = copy.blue
            this.opacity = copy.opacity
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = Color.Resolved(this as MutableStruct)

        override fun equals(other: Any?): Boolean {
            if (other !is Color.Resolved) return false
            return red == other.red && green == other.green && blue == other.blue && opacity == other.opacity
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, red)
            result = Hasher.combine(result, green)
            result = Hasher.combine(result, blue)
            result = Hasher.combine(result, opacity)
            return result
        }

        companion object {
        }
    }

    constructor(resolved: Color.Resolved): this(red = Double(resolved.red), green = Double(resolved.green), blue = Double(resolved.blue), opacity = Double(resolved.opacity)) {
    }


    // MARK: -

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(name: String, bundle: Any? = null) {
        colorImpl = { androidx.compose.ui.graphics.Color.White }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(uiColor: Any) {
        colorImpl = { androidx.compose.ui.graphics.Color.White }
    }

    // MARK: -

    override fun opacity(opacity: Double): Color {
        return Color(colorImpl = l@{
            val color = colorImpl()
            return@l color.copy(alpha = Float(opacity))
        })
    }

    // MARK: -

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val gradient: Any
        get() {
            fatalError()
        }

    override fun equals(other: Any?): Boolean {
        if (other !is Color) return false
        return colorImpl == other.colorImpl
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, colorImpl)
        return result
    }

    companion object {

        // MARK: -

        val accentColor: Color
            get() {
                return Color(colorImpl = { MaterialTheme.colorScheme.primary })
            }
        val clear = Color(colorImpl = { androidx.compose.ui.graphics.Color.Transparent })

        val white = Color(colorImpl = { androidx.compose.ui.graphics.Color.White })

        val black = Color(colorImpl = { androidx.compose.ui.graphics.Color.Black })

        val primary = Color(colorImpl = { MaterialTheme.colorScheme.onBackground })

        val secondary = Color(colorImpl = { MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium) })

        internal val systemBackground = Color(colorImpl = {
            // Matches Android's default bottom bar color
            MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        })

        internal val placeholder = Color(colorImpl = {
            // Close to iOS's AsyncImage placeholder values
            color(light = 0xFFDDDDDD, dark = 0xFF777777)
        })

        /// Returns the given color value based on whether the view is in dark mode or light mode
        @Composable
        private fun color(light: Long, dark: Long): androidx.compose.ui.graphics.Color {
            // TODO: EnvironmentValues.shared.colorMode == .dark ? dark : light
            return androidx.compose.ui.graphics.Color(if (isSystemInDarkTheme()) dark else light)
        }

        val gray = Color(colorImpl = { color(light = 0xFF8E8E93, dark = 0xFF8E8E93) })
        val red = Color(colorImpl = { color(light = 0xFFFF3B30, dark = 0xFFFF453A) })
        val orange = Color(colorImpl = { color(light = 0xFFFF9500, dark = 0xFFFF9F0A) })
        val yellow = Color(colorImpl = { color(light = 0xFFFFCC00, dark = 0xFFFFD60A) })
        val green = Color(colorImpl = { color(light = 0xFF34C759, dark = 0xFF30D158) })
        val mint = Color(colorImpl = { color(light = 0xFF00C7BE, dark = 0xFF63E6E2) })
        val teal = Color(colorImpl = { color(light = 0xFF30B0C7, dark = 0xFF40C8E0) })
        val cyan = Color(colorImpl = { color(light = 0xFF32ADE6, dark = 0xFF64D2FF) })
        val blue = Color(colorImpl = { color(light = 0xFF007AFF, dark = 0xFF0A84FF) })
        val indigo = Color(colorImpl = { color(light = 0xFF5856D6, dark = 0xFF5E5CE6) })
        val purple = Color(colorImpl = { color(light = 0xFFAF52DE, dark = 0xFFBF5AF2) })
        val pink = Color(colorImpl = { color(light = 0xFFFF2D55, dark = 0xFFFF375F) })
        val brown = Color(colorImpl = { color(light = 0xFFA2845E, dark = 0xFFAC8E68) })
    }
}

