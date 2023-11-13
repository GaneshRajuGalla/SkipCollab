// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Spacer: View {
    constructor() {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(minLength: Double?) {
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation-layout/src/commonMain/kotlin/androidx/compose/foundation/layout/Spacer.kt
    @Composable
    fun Spacer(modifier: Modifier)
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val fillModifier: Modifier
        var modifierBlock: @Composable (Boolean) -> Modifier
        if (EnvironmentValues.shared._fillWidthModifier != null) {
            fillModifier = EnvironmentValues.shared._fillWidth?.invoke(true) ?: Modifier
        } else if (EnvironmentValues.shared._fillHeightModifier != null) {
            fillModifier = EnvironmentValues.shared._fillHeight?.invoke(true) ?: Modifier
        } else {
            fillModifier = Modifier
        }
        val modifier = fillModifier.then(context.modifier)
        androidx.compose.foundation.layout.Spacer(modifier = modifier)
    }

    companion object {
    }
}
