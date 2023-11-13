// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

// Model Shape as a concrete type so that it can have static properties
open class Shape: View, Sendable {

    companion object {
        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val circle: Circle
            get() {
                fatalError()
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val rect: Rectangle
            get() {
                fatalError()
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun rect(cornerSize: CGSize, style: RoundedCornerStyle = RoundedCornerStyle.continuous): Rectangle {
            fatalError()
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun rect(cornerRadius: Double, style: RoundedCornerStyle = RoundedCornerStyle.continuous): Rectangle {
            fatalError()
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val capsule: Capsule
            get() {
                fatalError()
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun capsule(style: RoundedCornerStyle): Capsule {
            fatalError()
        }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val ellipse: Ellipse
            get() {
                fatalError()
            }
    }
}

class Circle: Shape {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(): super() {
    }


    companion object {
    }
}

class Rectangle: Shape {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(): super() {
    }


    companion object {
    }
}

class RoundedRectangle: Shape {
    val cornerSize: CGSize
    val style: RoundedCornerStyle

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(cornerSize: CGSize, style: RoundedCornerStyle = RoundedCornerStyle.continuous): super() {
        this.cornerSize = cornerSize.sref()
        this.style = style
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(cornerRadius: Double, style: RoundedCornerStyle = RoundedCornerStyle.continuous): super() {
        this.cornerSize = CGSize(width = cornerRadius, height = cornerRadius)
        this.style = style
    }


    companion object {
    }
}

class Capsule: Shape {
    val style: RoundedCornerStyle

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(style: RoundedCornerStyle = RoundedCornerStyle.continuous): super() {
        this.style = style
    }


    companion object {
    }
}

class Ellipse: Shape {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(): super() {
    }


    companion object {
    }
}

