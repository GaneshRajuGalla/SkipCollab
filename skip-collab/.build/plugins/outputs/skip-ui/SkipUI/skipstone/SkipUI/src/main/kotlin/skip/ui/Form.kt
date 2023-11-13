// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

class Form<Content>: View where Content: View {
    // It appears that on iOS, List and Form render the same
    internal val list: List<Content>

    constructor(content: () -> Content) {
        list = List(content = content)
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        list.Compose(context = context)
    }

    companion object {
    }
}

// Model `FormStyle` as a struct. Kotlin does not support static members of protocols
class FormStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FormStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = FormStyle(rawValue = 0)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val columns = FormStyle(rawValue = 1)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val grouped = FormStyle(rawValue = 2)
    }
}

