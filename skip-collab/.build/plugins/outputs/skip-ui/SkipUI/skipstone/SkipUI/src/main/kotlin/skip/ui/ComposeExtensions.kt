// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
/// Fill available remaining width.
///
/// This is a replacement for `fillMaxWidth` designed for situations when the composable may be in an `HStack` and does not want to push other items out.
///
/// - Warning: Containers with child content should use the `ComposeContainer` composable instead.

@Composable
internal fun Modifier.fillWidth(expandContainer: Boolean = true): Modifier {
    val fill_0 = EnvironmentValues.shared._fillWidth
    if (fill_0 == null) {
        return fillMaxWidth()
    }
    return then(fill_0(expandContainer))
}

/// Fill available remaining height.
///
/// This is a replacement for `fillMaxHeight` designed for situations when the composable may be in an `VStack` and does not want to push other items out.
///
/// - Warning: Containers with child content should use the `ComposeContainer` composable instead.
@Composable
internal fun Modifier.fillHeight(expandContainer: Boolean = true): Modifier {
    val fill_1 = EnvironmentValues.shared._fillHeight
    if (fill_1 == null) {
        return fillMaxHeight()
    }
    return then(fill_1(expandContainer))
}

/// Fill available remaining height.
///
/// This is a replacement for `fillMaxSize` designed for situations when the composable may be in an `HStack` or `VStack` and does not want to push other items out.
///
/// - Warning: Containers with child content should use the `ComposeContainer` composable instead.
@Composable
internal fun Modifier.fillSize(expandContainer: Boolean = true): Modifier = fillWidth(expandContainer = expandContainer).fillHeight(expandContainer = expandContainer)
