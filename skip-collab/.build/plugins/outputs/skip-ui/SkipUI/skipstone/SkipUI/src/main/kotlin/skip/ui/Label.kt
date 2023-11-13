// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

class Label: View, ListItemAdapting {
    internal val title: View
    internal val image: View

    constructor(title: () -> View, icon: () -> View) {
        this.title = title()
        this.image = icon()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, image: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        val name = image
        this.title = Text(titleKey)
        this.image = EmptyView()
    }

    constructor(titleKey: LocalizedStringKey, systemImage: String) {
        val name = systemImage
        this.title = Text(titleKey)
        this.image = Image(systemName = name)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, image: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        val name = image
        this.title = Text(title)
        this.image = EmptyView()
    }

    constructor(title: String, systemImage: String) {
        val name = systemImage
        this.title = Text(title)
        this.image = Image(systemName = name)
    }

    @Composable
    override fun ComposeContent(context: ComposeContext): Unit = ComposeLabel(context = context)

    @Composable
    private fun ComposeLabel(context: ComposeContext, imageColor: Color? = null, imageScale: Double? = null, titlePadding: Double = 0.0) {
        val imageModifier: Modifier
        if (imageScale != null) {
            imageModifier = Modifier.scale(scaleX = Float(imageScale), scaleY = Float(imageScale))
        } else {
            imageModifier = Modifier
        }
        Row(modifier = context.modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            if (imageColor != null) {
                EnvironmentValues.shared.setValues({ it -> it.set_color(imageColor) }, in_ = { image.Compose(context = context.content(modifier = imageModifier)) })
            } else {
                image.Compose(context = context.content(modifier = imageModifier))
            }
            Box(modifier = Modifier.padding(start = titlePadding.dp)) { title.Compose(context = context.content()) }
        }
    }

    /// Compose only the title of this label.
    @Composable
    internal fun ComposeTitle(context: ComposeContext): ComposeResult = title.Compose(context = context)

    /// Compose only the image of this label.
    @Composable
    internal fun ComposeImage(context: ComposeContext): ComposeResult = image.Compose(context = context)

    @Composable
    override fun shouldComposeListItem(): Boolean = true

    @Composable
    override fun ComposeListItem(context: ComposeContext, contentModifier: Modifier) {
        Box(modifier = contentModifier, contentAlignment = androidx.compose.ui.Alignment.CenterStart) { ComposeLabel(context = context, imageColor = EnvironmentValues.shared._listItemTint ?: Color.accentColor, imageScale = 1.25, titlePadding = 6.0) }
    }

    companion object {
    }
}

// Model `LabelStyle` as a struct. Kotlin does not support static members of protocols
class LabelStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LabelStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = LabelStyle(rawValue = 0)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val titleOnly = LabelStyle(rawValue = 1)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val iconOnly = LabelStyle(rawValue = 2)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val titleAndIcon = LabelStyle(rawValue = 3)
    }
}

// Erase the generic Label and Content to facilitate specialized constructor support.
class LabeledContent: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(content: () -> View, label: () -> View) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, content: () -> View) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, content: () -> View) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: String) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: String) {
    }

    companion object {
    }
}

// Model `LabeledContentStyle` as a struct. Kotlin does not support static members of protocols
class LabeledContentStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LabeledContentStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = LabeledContentStyle(rawValue = 0)
    }
}

