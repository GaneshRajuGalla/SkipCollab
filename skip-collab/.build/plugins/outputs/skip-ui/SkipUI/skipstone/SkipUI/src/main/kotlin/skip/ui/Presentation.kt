// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SheetPresentation(isPresented: Binding<Boolean>, content: () -> View, context: ComposeContext, onDismiss: (() -> Unit)?) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isPresented.get() || sheetState.isVisible) {
        val sheetDepth = EnvironmentValues.shared._sheetDepth
        ModalBottomSheet(onDismissRequest = { isPresented.set(false) }, sheetState = sheetState, containerColor = androidx.compose.ui.graphics.Color.Unspecified, dragHandle = null, windowInsets = WindowInsets(0, 0, 0, 0)) {
            val stateSaver = remember { mutableStateOf(ComposeStateSaver()) }
            val sheetDepth = EnvironmentValues.shared._sheetDepth
            val modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp - 20 * sheetDepth).dp)
                .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding())
            EnvironmentValues.shared.setValues({ it ->
                it.set_sheetDepth(sheetDepth + 1)
                it.setdismiss({ isPresented.set(false) })
            }, in_ = {
                Box(modifier = modifier, contentAlignment = androidx.compose.ui.Alignment.Center) { content().Compose(context = context.content(stateSaver = stateSaver.value)) }
            })
        }
    }
    if (!isPresented.get()) {
        LaunchedEffect(true) {
            if (sheetState.targetValue != SheetValue.Hidden) {
                sheetState.hide()
                onDismiss?.invoke()
            }
        }
    }
}

enum class PresentationAdaptation: Sendable {
    automatic,
    none,
    popover,
    sheet,
    fullScreenCover;

    companion object {
    }
}

class PresentationBackgroundInteraction: Sendable {
    internal val enabled: Boolean?
    internal val upThrough: PresentationDetent?

    internal constructor(enabled: Boolean? = null, upThrough: PresentationDetent? = null) {
        this.enabled = enabled
        this.upThrough = upThrough
    }

    companion object {

        val automatic = PresentationBackgroundInteraction(enabled = null, upThrough = null)

        val enabled = PresentationBackgroundInteraction(enabled = true, upThrough = null)

        fun enabled(upThrough: PresentationDetent): PresentationBackgroundInteraction = PresentationBackgroundInteraction(enabled = true, upThrough = upThrough)

        val disabled = PresentationBackgroundInteraction(enabled = false, upThrough = null)
    }
}

enum class PresentationContentInteraction: Sendable {
    automatic,
    resizes,
    scrolls;

    companion object {
    }
}

sealed class PresentationDetent: Sendable {
    class MediumCase: PresentationDetent() {
    }
    class LargeCase: PresentationDetent() {
    }
    class FractionCase(val associated0: Double): PresentationDetent() {
    }
    class HeightCase(val associated0: Double): PresentationDetent() {
    }
    class CustomCase(val associated0: KClass<*>): PresentationDetent() {
    }

    class Context {
        val maxDetentValue: Double

        constructor(maxDetentValue: Double) {
            this.maxDetentValue = maxDetentValue
        }

        //        public subscript<T>(dynamicMember keyPath: KeyPath<EnvironmentValues, T>) -> T { get { fatalError() } }

        companion object {
        }
    }

    override fun hashCode(): Int {
        var hasher = Hasher()
        hash(into = InOut<Hasher>({ hasher }, { hasher = it }))
        return hasher.finalize()
    }
    fun hash(into: InOut<Hasher>) {
        val hasher = into
        when (this) {
            is PresentationDetent.MediumCase -> hasher.value.combine(1)
            is PresentationDetent.LargeCase -> hasher.value.combine(2)
            is PresentationDetent.FractionCase -> {
                val fraction = this.associated0
                hasher.value.combine(3)
                hasher.value.combine(fraction)
            }
            is PresentationDetent.HeightCase -> {
                val height = this.associated0
                hasher.value.combine(4)
                hasher.value.combine(height)
            }
            is PresentationDetent.CustomCase -> {
                val type = this.associated0
                hasher.value.combine(String(describing = type))
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PresentationDetent) {
            return false
        }
        val lhs = this
        val rhs = other
        when (lhs) {
            is PresentationDetent.MediumCase -> {
                if (rhs is PresentationDetent.MediumCase) {
                    return true
                } else {
                    return false
                }
            }
            is PresentationDetent.LargeCase -> {
                if (rhs is PresentationDetent.LargeCase) {
                    return true
                } else {
                    return false
                }
            }
            is PresentationDetent.FractionCase -> {
                val fraction1 = lhs.associated0
                if (rhs is PresentationDetent.FractionCase) {
                    val fraction2 = rhs.associated0
                    return fraction1 == fraction2
                } else {
                    return false
                }
            }
            is PresentationDetent.HeightCase -> {
                val height1 = lhs.associated0
                if (rhs is PresentationDetent.HeightCase) {
                    val height2 = rhs.associated0
                    return height1 == height2
                } else {
                    return false
                }
            }
            is PresentationDetent.CustomCase -> {
                val type1 = lhs.associated0
                if (rhs is PresentationDetent.CustomCase) {
                    val type2 = rhs.associated0
                    return type1 == type2
                } else {
                    return false
                }
            }
        }
    }

    companion object {
        val medium: PresentationDetent = MediumCase()
        val large: PresentationDetent = LargeCase()
        fun fraction(associated0: Double): PresentationDetent = FractionCase(associated0)
        fun height(associated0: Double): PresentationDetent = HeightCase(associated0)
        fun custom(associated0: KClass<*>): PresentationDetent = CustomCase(associated0)
    }
}

interface CustomPresentationDetent {
}
