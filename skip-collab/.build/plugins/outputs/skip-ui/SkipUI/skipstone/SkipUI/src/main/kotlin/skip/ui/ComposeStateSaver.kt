// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope

/// Used in conjunction with `rememberSaveable` to save and restore state with SwiftUI-like behavior.
internal class ComposeStateSaver: Saver<Any?, Any> {
    private val state: MutableMap<ComposeStateSaver.Key, Any> = mutableMapOf()

    override fun restore(value: Any): Any? {
        if (value == Companion.nilMarker) {
            return null
        } else {
            val matchtarget_0 = value as? ComposeStateSaver.Key
            if (matchtarget_0 != null) {
                val key = matchtarget_0
                return state[key].sref()
            } else {
                return value.sref()
            }
        }
    }

    override fun SaverScope.save(value: Any?): Any {
        if (value == null) {
            return Companion.nilMarker
        } else if (value is Boolean || value is Number || value is String || value is Char) {
            return value.sref()
        } else {
            val key = Key.next()
            state[key] = value.sref()
            return key.sref()
        }
    }

    /// Key under which to save values that cannot be stored directly in the Bundle.
    private class Key: Parcelable {

        private val value: Int

        private constructor(value: Int) {
            this.value = value
        }

        internal constructor(parcel: Parcel): this(parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int): Unit = parcel.writeInt(value)

        override fun describeContents(): Int = 0

        private open class Creator: Parcelable.Creator<ComposeStateSaver.Key> {
            override fun createFromParcel(parcel: Parcel): ComposeStateSaver.Key = Key(parcel)

            override fun newArray(size: Int): kotlin.Array<ComposeStateSaver.Key?> = arrayOfNulls(size)
        }

        companion object {
            private var keyValue = 0

            internal fun next(): ComposeStateSaver.Key {
                keyValue += 1
                return Key(value = keyValue)
            }

            internal val CREATOR: Parcelable.Creator<ComposeStateSaver.Key> = Creator()
        }
    }

    companion object {
        private val nilMarker = "__SkipUI.ComposeStateSaver.nilMarker"
    }
}
