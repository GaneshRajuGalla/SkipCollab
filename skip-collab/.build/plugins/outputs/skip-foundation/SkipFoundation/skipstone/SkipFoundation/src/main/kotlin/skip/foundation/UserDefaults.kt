// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
internal typealias PlatformUserDefaults = android.content.SharedPreferences

open class UserDefaults {
    internal val platformValue: android.content.SharedPreferences
    /// The default default values
    private var registrationDictionary: Dictionary<String, Any> = dictionaryOf()
        get() = field.sref({ this.registrationDictionary = it })
        set(newValue) {
            field = newValue.sref()
        }

    internal constructor(platformValue: android.content.SharedPreferences) {
        this.platformValue = platformValue
    }

    internal constructor(platformValue: android.content.SharedPreferences, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.platformValue = platformValue
    }

    constructor(suiteName: String?) {
        platformValue = ProcessInfo.processInfo.androidContext.getSharedPreferences(suiteName ?: "defaults", android.content.Context.MODE_PRIVATE)
    }

    open fun register(defaults: Dictionary<String, Any>) {
        val registrationDictionary = defaults
        this.registrationDictionary = registrationDictionary
    }

    open fun registerOnSharedPreferenceChangeListener(key: String, onChange: () -> Unit): Any {
        val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey: String? ->
            if ((changedKey != null) && (key == changedKey)) {
                onChange()
            }
        }

        platformValue.registerOnSharedPreferenceChangeListener(listener)
        return listener
    }

    open fun `set`(value: Int, forKey: String) {
        val defaultName = forKey
        val prefs = platformValue.edit()
        prefs.putInt(defaultName, value)
        prefs.apply()
    }

    open fun `set`(value: Boolean, forKey: String) {
        val defaultName = forKey
        val prefs = platformValue.edit()
        prefs.putBoolean(defaultName, value)
        prefs.apply()
    }

    open fun `set`(value: Double, forKey: String) {
        val defaultName = forKey
        val prefs = platformValue.edit()
        prefs.putFloat(defaultName, value.toFloat())
        prefs.apply()
    }

    open fun `set`(value: String, forKey: String) {
        val defaultName = forKey
        val prefs = platformValue.edit()
        prefs.putString(defaultName, value)
        prefs.apply()
    }

    open fun `set`(value: Any?, forKey: String) {
        val defaultName = forKey
        var deferaction_0: (() -> Unit)? = null
        try {
            val prefs = platformValue.edit()
            deferaction_0 = {
                prefs.apply()
            }

            val matchtarget_0 = value as? Float
            if (matchtarget_0 != null) {
                val v = matchtarget_0
                prefs.putFloat(defaultName, v.toFloat())
            } else {
                val matchtarget_1 = value as? Long
                if (matchtarget_1 != null) {
                    val v = matchtarget_1
                    prefs.putLong(defaultName, v)
                } else {
                    val matchtarget_2 = value as? Int
                    if (matchtarget_2 != null) {
                        val v = matchtarget_2
                        prefs.putInt(defaultName, v)
                    } else {
                        val matchtarget_3 = value as? Boolean
                        if (matchtarget_3 != null) {
                            val v = matchtarget_3
                            prefs.putBoolean(defaultName, v)
                        } else {
                            val matchtarget_4 = value as? Double
                            if (matchtarget_4 != null) {
                                val v = matchtarget_4
                                prefs.putString(defaultName, v.toString())
                            } else {
                                val matchtarget_5 = value as? Number
                                if (matchtarget_5 != null) {
                                    val v = matchtarget_5
                                    prefs.putString(defaultName, v.toString())
                                } else {
                                    val matchtarget_6 = value as? String
                                    if (matchtarget_6 != null) {
                                        val v = matchtarget_6
                                        prefs.putString(defaultName, v)
                                    } else {
                                        val matchtarget_7 = value as? URL
                                        if (matchtarget_7 != null) {
                                            val v = matchtarget_7
                                            prefs.putString(defaultName, v.absoluteString)
                                        } else {
                                            val matchtarget_8 = value as? Data
                                            if (matchtarget_8 != null) {
                                                val v = matchtarget_8
                                                prefs.putString(defaultName, v.base64EncodedString())
                                            } else {
                                                val matchtarget_9 = value as? Date
                                                if (matchtarget_9 != null) {
                                                    val v = matchtarget_9
                                                    prefs.putString(defaultName, v.ISO8601Format())
                                                } else {
                                                    // we ignore
                                                    return
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            deferaction_0?.invoke()
        }
    }

    open fun removeObject(forKey: String) {
        val defaultName = forKey
        val prefs = platformValue.edit()
        prefs.remove(defaultName)
        prefs.apply()
    }

    /// Returns the value from the current defaults.
    /// Called `object_` since `object` is an unescapable keyword in Kotin.
    open fun object_(forKey: String): Any? {
        val defaultName = forKey
        return (platformValue.getAll()[defaultName] ?: registrationDictionary[defaultName] ?: null).sref()
    }

    open fun string(forKey: String): String? {
        val defaultName = forKey
        val value_0 = object_(forKey = defaultName)
        if (value_0 == null) {
            return null
        }
        val matchtarget_10 = value_0 as? Number
        if (matchtarget_10 != null) {
            val number = matchtarget_10
            return number.toString()
        } else {
            val matchtarget_11 = value_0 as? Boolean
            if (matchtarget_11 != null) {
                val bool = matchtarget_11
                return if (bool) "YES" else "NO"
            } else {
                val matchtarget_12 = value_0 as? String
                if (matchtarget_12 != null) {
                    val string = matchtarget_12
                    return string
                } else {
                    return null
                }
            }
        }
    }

    open fun double(forKey: String): Double? {
        val defaultName = forKey
        val value_1 = object_(forKey = defaultName)
        if (value_1 == null) {
            return null
        }
        val matchtarget_13 = value_1 as? Number
        if (matchtarget_13 != null) {
            val number = matchtarget_13
            return number.toDouble()
        } else {
            val matchtarget_14 = value_1 as? Boolean
            if (matchtarget_14 != null) {
                val bool = matchtarget_14
                return if (bool) 1.0 else 0.0
            } else {
                val matchtarget_15 = value_1 as? String
                if (matchtarget_15 != null) {
                    val string = matchtarget_15
                    return string.toDouble()
                } else {
                    return null
                }
            }
        }
    }

    open fun integer(forKey: String): Int? {
        val defaultName = forKey
        val value_2 = object_(forKey = defaultName)
        if (value_2 == null) {
            return null
        }
        val matchtarget_16 = value_2 as? Number
        if (matchtarget_16 != null) {
            val number = matchtarget_16
            return number.toInt()
        } else {
            val matchtarget_17 = value_2 as? Boolean
            if (matchtarget_17 != null) {
                val bool = matchtarget_17
                return if (bool) 1 else 0
            } else {
                val matchtarget_18 = value_2 as? String
                if (matchtarget_18 != null) {
                    val string = matchtarget_18
                    return string.toInt()
                } else {
                    return null
                }
            }
        }
    }

    open fun bool(forKey: String): Boolean? {
        val defaultName = forKey
        val value_3 = object_(forKey = defaultName)
        if (value_3 == null) {
            return null
        }
        val matchtarget_19 = value_3 as? Number
        if (matchtarget_19 != null) {
            val number = matchtarget_19
            return if (number.toDouble() == 0.0) false else true
        } else {
            val matchtarget_20 = value_3 as? Boolean
            if (matchtarget_20 != null) {
                val bool = matchtarget_20
                return bool
            } else {
                val matchtarget_21 = value_3 as? String
                if (matchtarget_21 != null) {
                    val string = matchtarget_21
                    // match the default string->bool conversion for UserDefaults
                    return arrayOf("true", "yes", "1").contains(string.lowercased())
                } else {
                    return null
                }
            }
        }
    }

    open fun url(forKey: String): URL? {
        val defaultName = forKey
        val value_4 = object_(forKey = defaultName)
        if (value_4 == null) {
            return null
        }
        val matchtarget_22 = value_4 as? URL
        if (matchtarget_22 != null) {
            val url = matchtarget_22
            return url.sref()
        } else {
            val matchtarget_23 = value_4 as? String
            if (matchtarget_23 != null) {
                val string = matchtarget_23
                return URL(string = string)
            } else {
                return null
            }
        }
    }

    open fun data(forKey: String): Data? {
        val defaultName = forKey
        val value_5 = object_(forKey = defaultName)
        if (value_5 == null) {
            return null
        }
        val matchtarget_24 = value_5 as? Data
        if (matchtarget_24 != null) {
            val url = matchtarget_24
            return url.sref()
        } else {
            val matchtarget_25 = value_5 as? String
            if (matchtarget_25 != null) {
                val string = matchtarget_25
                return (try { Data(base64Encoded = string) } catch (_: NullReturnException) { null })
            } else {
                return null
            }
        }
    }

    open fun kotlin(nocopy: Boolean = false): android.content.SharedPreferences = platformValue

    companion object {

        val standard: UserDefaults
            get() = UserDefaults(suiteName = null)
    }
}



internal fun android.content.SharedPreferences.swift(nocopy: Boolean = false): UserDefaults = UserDefaults(platformValue = this)
