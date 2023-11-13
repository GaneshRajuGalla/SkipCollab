// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import skip.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// Erase the generic to facilitate specialization.
interface Gesture {

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun exclusively(before: Gesture): Gesture {
        val other = before
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onEnded(action: () -> Unit): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onEnded(action: (Any) -> Unit): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onChanged(action: () -> Unit): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onChanged(action: (Any) -> Unit): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun map(body: () -> Any): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun map(body: (Any) -> Any): Gesture = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun sequenced(before: Gesture): Gesture {
        val other = before
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun simultaneously(with: Gesture): Gesture {
        val other = with
        return this.sref()
    }
}

class DragGesture: Gesture, MutableStruct {
    class Value: Sendable, MutableStruct {
        var time: Date
            get() = field.sref({ this.time = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var location: CGPoint
            get() = field.sref({ this.location = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var startLocation: CGPoint
            get() = field.sref({ this.startLocation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var translation: CGSize
            get() = field.sref({ this.translation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var velocity: CGSize
            get() = field.sref({ this.velocity = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var predictedEndLocation: CGPoint
            get() = field.sref({ this.predictedEndLocation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var predictedEndTranslation: CGSize
            get() = field.sref({ this.predictedEndTranslation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(time: Date, location: CGPoint, startLocation: CGPoint, translation: CGSize, velocity: CGSize, predictedEndLocation: CGPoint, predictedEndTranslation: CGSize) {
            this.time = time
            this.location = location
            this.startLocation = startLocation
            this.translation = translation
            this.velocity = velocity
            this.predictedEndLocation = predictedEndLocation
            this.predictedEndTranslation = predictedEndTranslation
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = DragGesture.Value(time, location, startLocation, translation, velocity, predictedEndLocation, predictedEndTranslation)

        override fun equals(other: Any?): Boolean {
            if (other !is DragGesture.Value) return false
            return time == other.time && location == other.location && startLocation == other.startLocation && translation == other.translation && velocity == other.velocity && predictedEndLocation == other.predictedEndLocation && predictedEndTranslation == other.predictedEndTranslation
        }

        companion object {
        }
    }

    var minimumDistance: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var coordinateSpace: CoordinateSpaceProtocol
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(minimumDistance: Double = 10.0, coordinateSpace: CoordinateSpaceProtocol = CoordinateSpaceProtocol.local) {
        this.minimumDistance = minimumDistance
        this.coordinateSpace = coordinateSpace
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as DragGesture
        this.minimumDistance = copy.minimumDistance
        this.coordinateSpace = copy.coordinateSpace
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = DragGesture(this as MutableStruct)

    companion object {
    }
}

class TapGesture: Gesture, MutableStruct {
    var count: Int
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(count: Int = 1) {
        this.count = count
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as TapGesture
        this.count = copy.count
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = TapGesture(this as MutableStruct)

    companion object {
    }
}

class LongPressGesture: Gesture, MutableStruct {
    var minimumDuration: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var maximumDistance: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(minimumDuration: Double = 0.5, maximumDistance: Double = 10.0) {
        this.minimumDuration = minimumDuration
        this.maximumDistance = maximumDistance
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as LongPressGesture
        this.minimumDuration = copy.minimumDuration
        this.maximumDistance = copy.maximumDistance
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = LongPressGesture(this as MutableStruct)

    companion object {
    }
}

class MagnifyGesture: Gesture, MutableStruct {
    class Value: Sendable, MutableStruct {
        var time: Date
            get() = field.sref({ this.time = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var magnification: Double
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }
        var velocity: Double
            set(newValue) {
                willmutate()
                field = newValue
                didmutate()
            }
        var startAnchor: UnitPoint
            get() = field.sref({ this.startAnchor = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var startLocation: CGPoint
            get() = field.sref({ this.startLocation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(time: Date, magnification: Double, velocity: Double, startAnchor: UnitPoint, startLocation: CGPoint) {
            this.time = time
            this.magnification = magnification
            this.velocity = velocity
            this.startAnchor = startAnchor
            this.startLocation = startLocation
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = MagnifyGesture.Value(time, magnification, velocity, startAnchor, startLocation)

        override fun equals(other: Any?): Boolean {
            if (other !is MagnifyGesture.Value) return false
            return time == other.time && magnification == other.magnification && velocity == other.velocity && startAnchor == other.startAnchor && startLocation == other.startLocation
        }

        companion object {
        }
    }

    var minimumScaleDelta: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(minimumScaleDelta: Double = 0.01) {
        this.minimumScaleDelta = minimumScaleDelta
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as MagnifyGesture
        this.minimumScaleDelta = copy.minimumScaleDelta
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = MagnifyGesture(this as MutableStruct)

    companion object {
    }
}

class RotateGesture: Gesture, MutableStruct {
    class Value: Sendable, MutableStruct {
        var time: Date
            get() = field.sref({ this.time = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var rotation: Angle
            get() = field.sref({ this.rotation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var velocity: Angle
            get() = field.sref({ this.velocity = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var startAnchor: UnitPoint
            get() = field.sref({ this.startAnchor = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }
        var startLocation: CGPoint
            get() = field.sref({ this.startLocation = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(time: Date, rotation: Angle, velocity: Angle, startAnchor: UnitPoint, startLocation: CGPoint) {
            this.time = time
            this.rotation = rotation
            this.velocity = velocity
            this.startAnchor = startAnchor
            this.startLocation = startLocation
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = RotateGesture.Value(time, rotation, velocity, startAnchor, startLocation)

        override fun equals(other: Any?): Boolean {
            if (other !is RotateGesture.Value) return false
            return time == other.time && rotation == other.rotation && velocity == other.velocity && startAnchor == other.startAnchor && startLocation == other.startLocation
        }

        companion object {
        }
    }

    var minimumAngleDelta: Angle
        get() = field.sref({ this.minimumAngleDelta = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(minimumAngleDelta: Angle = Angle.degrees(1.0)) {
        this.minimumAngleDelta = minimumAngleDelta
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as RotateGesture
        this.minimumAngleDelta = copy.minimumAngleDelta
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = RotateGesture(this as MutableStruct)

    companion object {
    }
}

class SpatialEventGesture: Gesture {
    val coordinateSpace: CoordinateSpaceProtocol
    val action: (Any) -> Unit

    constructor(coordinateSpace: CoordinateSpaceProtocol = CoordinateSpaceProtocol.local, action: (Any) -> Unit) {
        this.coordinateSpace = coordinateSpace
        this.action = action
    }


    companion object {
    }
}

class SpatialTapGesture: Gesture, MutableStruct {
    class Value: Sendable, MutableStruct {
        var location: CGPoint
            get() = field.sref({ this.location = it })
            set(newValue) {
                @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
                willmutate()
                field = newValue
                didmutate()
            }

        constructor(location: CGPoint) {
            this.location = location
        }

        override var supdate: ((Any) -> Unit)? = null
        override var smutatingcount = 0
        override fun scopy(): MutableStruct = SpatialTapGesture.Value(location)

        override fun equals(other: Any?): Boolean {
            if (other !is SpatialTapGesture.Value) return false
            return location == other.location
        }

        companion object {
        }
    }

    var count: Int
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var coordinateSpace: CoordinateSpaceProtocol
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(count: Int = 1, coordinateSpace: CoordinateSpaceProtocol = CoordinateSpaceProtocol.local) {
        this.count = count
        this.coordinateSpace = coordinateSpace
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as SpatialTapGesture
        this.count = copy.count
        this.coordinateSpace = copy.coordinateSpace
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = SpatialTapGesture(this as MutableStruct)

    companion object {
    }
}

class GestureMask: OptionSet<GestureMask, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): GestureMask = GestureMask(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: GestureMask): Unit = assignfrom(target)

    private fun assignfrom(target: GestureMask) {
        this.rawValue = target.rawValue
    }

    companion object {

        val none = GestureMask(rawValue = 1)
        val gesture = GestureMask(rawValue = 2)
        val subviews = GestureMask(rawValue = 4)
        val all = GestureMask(rawValue = 7)

        fun of(vararg options: GestureMask): GestureMask {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return GestureMask(rawValue = value)
        }
    }
}

internal open class GestureModifierView: ComposeModifierView {
    internal open var tapAction: ((CGPoint) -> Unit)? = null
    internal open var doubleTapAction: ((CGPoint) -> Unit)? = null
    internal open var longPressAction: (() -> Unit)? = null

    internal constructor(contextView: View, tapAction: ((CGPoint) -> Unit)? = null, doubleTapAction: ((CGPoint) -> Unit)? = null, longPressAction: (() -> Unit)? = null): super(contextView = contextView, role = ComposeModifierRole.gesture, contextTransform = { _ ->  }) {
        // Compose doesn't support multiple pointerInput modifiers, so combine them into this view
        val wrappedGestureView = contextView.strippingModifiers(until = { it -> it == ComposeModifierRole.gesture }, perform = { it -> it as? GestureModifierView })
        this.tapAction = tapAction ?: wrappedGestureView?.tapAction
        this.doubleTapAction = doubleTapAction ?: wrappedGestureView?.doubleTapAction
        this.longPressAction = longPressAction ?: wrappedGestureView?.longPressAction
        wrappedGestureView?.tapAction = null
        wrappedGestureView?.doubleTapAction = null
        wrappedGestureView?.longPressAction = null

        this.contextTransform = { it -> it.value.modifier = addGestures(to = it.value.modifier) }
    }

    @Composable
    private fun addGestures(to: Modifier): Modifier {
        val modifier = to
        if (tapAction == null && doubleTapAction == null && longPressAction == null) {
            return modifier
        }
        if (!EnvironmentValues.shared.isEnabled) {
            return modifier
        }
        val density = LocalDensity.current.sref()
        return modifier.pointerInput(true) {
            val onDoubleTap: ((Offset) -> Unit)? = linvoke l@{
                val matchtarget_0 = doubleTapAction
                if (matchtarget_0 != null) {
                    val doubleTapAction = matchtarget_0
                    return@l { offset ->
                        val x = with(density) { offset.x.toDp() }
                        val y = with(density) { offset.y.toDp() }
                        doubleTapAction(CGPoint(x = Double(x.value), y = Double(y.value)))
                    }
                } else {
                    return@l null
                }
            }
            val onLongPress: ((Offset) -> Unit)? = linvoke l@{
                val matchtarget_1 = longPressAction
                if (matchtarget_1 != null) {
                    val longPressAction = matchtarget_1
                    return@l { _ -> longPressAction() }
                } else {
                    return@l null
                }
            }
            detectTapGestures(onDoubleTap = onDoubleTap, onLongPress = onLongPress) { offset ->
                tapAction?.let { tapAction ->
                    val x = with(density) { offset.x.toDp() }
                    val y = with(density) { offset.y.toDp() }
                    tapAction(CGPoint(x = Double(x.value), y = Double(y.value)))
                }
            }
        }
    }
}

