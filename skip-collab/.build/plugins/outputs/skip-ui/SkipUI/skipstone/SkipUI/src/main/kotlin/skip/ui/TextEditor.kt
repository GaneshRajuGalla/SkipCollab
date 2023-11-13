// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class TextEditor: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(text: Binding<String>) {
    }


    companion object {
    }
}

// Model `TextEditorStyle` as a struct. Kotlin does not support static members of protocols
class TextEditorStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TextEditorStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = TextEditorStyle(rawValue = 0)
        val plain = TextEditorStyle(rawValue = 1)
    }
}

