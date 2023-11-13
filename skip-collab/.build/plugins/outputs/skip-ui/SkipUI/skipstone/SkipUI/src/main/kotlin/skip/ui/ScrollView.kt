// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Set

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class ScrollView<Content>: View where Content: View {
    internal val content: Content
    internal val axes: Axis.Set

    constructor(axes: Axis.Set = Axis.Set.vertical, content: () -> Content) {
        this.axes = axes
        this.content = content()
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val scrollState = rememberScrollState()
        var scrollModifier: Modifier = Modifier
        if (axes.contains(Axis.Set.vertical)) {
            scrollModifier = scrollModifier.verticalScroll(scrollState)
        }
        if (axes.contains(Axis.Set.horizontal)) {
            scrollModifier = scrollModifier.horizontalScroll(scrollState)
        }
        val contentContext = context.content()
        ComposeContainer(modifier = context.modifier, fillWidth = axes.contains(Axis.Set.horizontal), fillHeight = axes.contains(Axis.Set.vertical), then = scrollModifier) { modifier ->
            Box(modifier = modifier) { content.Compose(context = contentContext) }
        }
    }

    companion object {
    }
}

enum class ScrollBounceBehavior: Sendable {
    automatic,
    always,
    basedOnSize;

    companion object {
    }
}

enum class ScrollDismissesKeyboardMode: Sendable {
    automatic,
    immediately,
    interactively,
    never;

    companion object {
    }
}

enum class ScrollIndicatorVisibility {
    automatic,
    visible,
    hidden,
    never;

    companion object {
    }
}

class ScrollTarget: MutableStruct {
    var rect: CGRect
        get() = field.sref({ this.rect = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var anchor: UnitPoint? = null
        get() = field.sref({ this.anchor = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(rect: CGRect, anchor: UnitPoint? = null) {
        this.rect = rect
        this.anchor = anchor
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as ScrollTarget
        this.rect = copy.rect
        this.anchor = copy.anchor
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ScrollTarget(this as MutableStruct)

    companion object {
    }
}

class PinnedScrollableViews: OptionSet<PinnedScrollableViews, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): PinnedScrollableViews = PinnedScrollableViews(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: PinnedScrollableViews): Unit = assignfrom(target)

    private fun assignfrom(target: PinnedScrollableViews) {
        this.rawValue = target.rawValue
    }

    companion object {

        val sectionHeaders = PinnedScrollableViews(rawValue = 1 shl 0)
        val sectionFooters = PinnedScrollableViews(rawValue = 1 shl 1)

        fun of(vararg options: PinnedScrollableViews): PinnedScrollableViews {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return PinnedScrollableViews(rawValue = value)
        }
    }
}

