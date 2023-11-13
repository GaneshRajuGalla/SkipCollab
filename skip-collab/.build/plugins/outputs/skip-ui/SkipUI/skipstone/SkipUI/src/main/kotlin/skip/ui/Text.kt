// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import skip.foundation.LocalizedStringResource
import skip.foundation.Bundle

class Text: View, Sendable {
    internal val text: String

    constructor(verbatim: String) {
        this.text = verbatim
    }

    constructor(text: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.text = text
    }

    constructor(key: LocalizedStringKey, bundle: Bundle? = null, comment: String? = null) {
        this.text = key.value
    }

    constructor(resource: LocalizedStringResource) {
        this.text = resource.key
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Text.kt
    @Composable
    fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        var font: Font
        var text = this.text
        val matchtarget_0 = EnvironmentValues.shared.font
        if (matchtarget_0 != null) {
            val environmentFont = matchtarget_0
            font = environmentFont
        } else {
            val matchtarget_1 = EnvironmentValues.shared._listSectionHeaderStyle
            if (matchtarget_1 != null) {
                val sectionHeaderStyle = matchtarget_1
                font = Font.callout
                if (sectionHeaderStyle == ListStyle.plain) {
                    font = font.bold()
                } else {
                    text = text.uppercased()
                }
            } else {
                val matchtarget_2 = EnvironmentValues.shared._listSectionFooterStyle
                if (matchtarget_2 != null) {
                    val sectionFooterStyle = matchtarget_2
                    if (sectionFooterStyle != ListStyle.plain) {
                        font = Font.footnote
                    } else {
                        font = Font(fontImpl = { LocalTextStyle.current })
                    }
                } else {
                    font = Font(fontImpl = { LocalTextStyle.current })
                }
            }
        }
        EnvironmentValues.shared._fontWeight?.let { weight ->
            font = font.weight(weight)
        }
        if (EnvironmentValues.shared._isItalic) {
            font = font.italic()
        }

        val textColor: Color
        val matchtarget_3 = EnvironmentValues.shared._color
        if (matchtarget_3 != null) {
            val environmentColor = matchtarget_3
            textColor = environmentColor
        } else if (EnvironmentValues.shared._listSectionHeaderStyle != null) {
            textColor = Color.secondary
        } else {
            val matchtarget_4 = EnvironmentValues.shared._listSectionFooterStyle
            if (matchtarget_4 != null) {
                val sectionFooterStyle = matchtarget_4
                if (sectionFooterStyle != ListStyle.plain) {
                    textColor = Color.secondary
                } else {
                    textColor = Color(colorImpl = { androidx.compose.ui.graphics.Color.Unspecified })
                }
            } else {
                textColor = Color(colorImpl = { androidx.compose.ui.graphics.Color.Unspecified })
            }
        }
        val modifier = context.modifier
        val maxLines = max(1, EnvironmentValues.shared.lineLimit ?: Int.MAX_VALUE)
        androidx.compose.material3.Text(text = text, modifier = modifier, color = textColor.colorImpl(), maxLines = maxLines, style = font.fontImpl())
    }

    enum class Case: Sendable {
        uppercase,
        lowercase;

        companion object {
        }
    }

    class LineStyle: Sendable {
        val pattern: Text.LineStyle.Pattern
        val color: Color?

        constructor(pattern: Text.LineStyle.Pattern = Text.LineStyle.Pattern.solid, color: Color? = null) {
            this.pattern = pattern
            this.color = color
        }

        enum class Pattern: Sendable {
            solid,
            dot,
            dash,
            dashot,
            dashDotDot;

            companion object {
            }
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Text.LineStyle) return false
            return pattern == other.pattern && color == other.color
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, pattern)
            result = Hasher.combine(result, color)
            return result
        }

        companion object {

            val single = Text.LineStyle()
        }
    }

    enum class Scale: Sendable {
        default,
        secondary;

        companion object {
        }
    }

    enum class TruncationMode: Sendable {
        head,
        tail,
        middle;

        companion object {
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Text) return false
        return text == other.text
    }

    companion object {
    }
}

enum class TextAlignment: CaseIterable, Sendable {
    leading,
    center,
    trailing;

    companion object {
        val allCases: Array<TextAlignment>
            get() = arrayOf(leading, center, trailing)
    }
}

