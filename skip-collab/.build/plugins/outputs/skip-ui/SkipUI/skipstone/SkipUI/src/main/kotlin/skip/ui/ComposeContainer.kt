// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/// Composable to handle sizing and layout in a SwiftUI-like way for containers that compose child content.
///
/// Compose's behavior differs from SwiftUI's when dealing with filling space. A SwiftUI container will give each child the
/// space it needs to display, then automatically divide the remainder between children that want to expand. In Compose, on
/// the other hand, a single 'fillMaxWidth' child will consume all remaining space, pushing subsequent children out. To get
/// SwiftUI's behavior, all children that want to expand must use the `weight` modifier, which is only available in a Row or
/// Column scope. We've abstracted the fact that 'weight' for a given dimension may or may not be available depending on scope
/// behind our `Modifier.fillWidth` and `Modifier.fillHeight` extension functions.
///
/// Having to explicitly set a certain modifier in order to expand within a parent is problematic for containers that want to
/// fit content. The container only wants to expand if it has content that wants to expand. It cant't know this until it composes
/// its content. The code in this function sets triggers on the environment values that we use in 'fillWidth' and 'fillHeight' so
/// that if the container content uses them, the container itself can recompose with the appropriate expansion to match its
/// content. Note that this generally only affects final layout when an expanding child is in a container that is itself in a
/// container, and it has to share space with other members of the parent container.
@Composable
fun ComposeContainer(modifier: Modifier = Modifier, fillWidth: Boolean = false, fillHeight: Boolean = false, then: Modifier = Modifier, content: @Composable (Modifier) -> Unit) {
    // Use remembered expansion values to recompose on change
    val isFillWidth = remember { mutableStateOf(fillWidth) }
    val isNonExpandingFillWidth = remember { mutableStateOf(false) }
    val isFillHeight = remember { mutableStateOf(fillHeight) }
    val isNonExpandingFillHeight = remember { mutableStateOf(false) }

    // Create the correct modifier for the current values. We use IntrinsicSize.Max for non-expanding fills so that child views who want to
    // take up available space without expanding this container can do so by calling `fillMaxWidth/Height`
    var modifier = modifier
    if (isFillWidth.value) {
        modifier = modifier.fillWidth()
    } else if (isNonExpandingFillWidth.value) {
        modifier = modifier.width(IntrinsicSize.Max)
    }
    if (isFillHeight.value) {
        modifier = modifier.fillHeight()
    } else if (isNonExpandingFillHeight.value) {
        modifier = modifier.height(IntrinsicSize.Max)
    }
    modifier = modifier.then(then)

    EnvironmentValues.shared.setValues({ it ->
        // Setup the initial environment before rendering the container content. First, we reset the any saved fill modifiers because
        // this is a new container. A directional container like 'HStack' or 'VStack' will set the correct modifier before rendering
        // in the content block below, so that its own children can distribute available space
        it.set_fillWidthModifier(null)
        it.set_fillHeightModifier(null)

        // Set the 'fillWidth' and 'fillHeight' blocks to trigger a side effect to update our container's expansion state, which can
        // cause it to recompose and recalculate its own modifier. We must use `SideEffect` or the recomposition never happens
        it.set_fillWidth l@{ expandContainer ->
            if (expandContainer && !isFillWidth.value) {
                SideEffect { isFillWidth.value = true }
            }
            if (!expandContainer && !isNonExpandingFillWidth.value) {
                SideEffect { isNonExpandingFillWidth.value = true }
            }
            if (!expandContainer) {
                return@l Modifier.fillMaxWidth()
            } else {
                return@l EnvironmentValues.shared._fillWidthModifier ?: Modifier.fillMaxWidth()
            }
        }
        it.set_fillHeight l@{ expandContainer ->
            if (expandContainer && !isFillHeight.value) {
                SideEffect { isFillHeight.value = true }
            }
            if (!expandContainer && !isNonExpandingFillHeight.value) {
                SideEffect { isNonExpandingFillHeight.value = true }
            }
            if (!expandContainer) {
                return@l Modifier.fillMaxHeight()
            } else {
                return@l EnvironmentValues.shared._fillHeightModifier ?: Modifier.fillMaxHeight()
            }
        }
    }, in_ = {
        // Render the container content with the above environment setup
        content(modifier)
    })
}
