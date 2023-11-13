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

open class ProcessInfo {

    internal constructor() {
    }

    /// The Android context for the process, which should have been set on app launch, and will fall back on using an Android test context.
    open val androidContext: android.content.Context
        get() = androidContextstorage!!
    private val androidContextstorage: android.content.Context?
        get() {
            // androidx.compose.ui.platform.LocalContext.current could be used, but it is @Composable and so can't be called from a static context
            return launchContext ?: testContext
        }

    private val testContext: android.content.Context
        get() {
            // fallback to assuming we are running in a test environment
            // we don't have a compile dependency on android test, so we need to load using reflection
            // androidx.test.core.app.ApplicationProvider.getApplicationContext()
            return Class.forName("androidx.test.core.app.ApplicationProvider")
                .getDeclaredMethod("getApplicationContext")
                .invoke(null) as android.content.Context
        }

    private var launchContext: android.content.Context? = null
        get() = field.sref({ this.launchContext = it })
        set(newValue) {
            field = newValue.sref()
        }


    open val globallyUniqueString: String
        get() = UUID().description

    private val systemProperties: Dictionary<String, String> = Companion.buildSystemProperties()

    open val environment: Dictionary<String, String>
        get() = systemProperties

    open val processIdentifier: Int
        get() {
            try {
                return android.os.Process.myPid()
            } catch (error: Throwable) {
                @Suppress("NAME_SHADOWING") val error = error.aserror()
                // seems to happen in Robolectric tests
                // return java.lang.ProcessHandle.current().pid().toInt() // JDK9+, so doesn't compile
                // JMX name is "pid@hostname" (e.g., "57924@zap.local")
                // return java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split(separator: "@").first?.toLong() ?? -1
                return -1
            }
        }

    open val arguments: Array<String>
        get() = arrayOf() // no arguments on Android

    open val hostName: String
        get() {
            // Android 30+: NetworkOnMainThreadException
            return java.net.InetAddress.getLocalHost().hostName
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val processName: String
        get() {
            return fatalError("TODO: ProcessInfo")
        }

    open val processorCount: Int
        get() = Runtime.getRuntime().availableProcessors()

    open val operatingSystemVersionString: String
        get() = android.os.Build.VERSION.RELEASE

    companion object {
        /// The global `processInfo` must be set manually at app launch with `skip.foundation.ProcessInfo.launch(context)`
        /// Otherwise error: `skip.lib.ErrorException: kotlin.UninitializedPropertyAccessException: lateinit property processInfo has not been initialized`
        var processInfo: ProcessInfo = ProcessInfo()

        /// Called when an app is launched to store the global context from the `android.app.Application` subclass.
        fun launch(context: android.content.Context) {
            ProcessInfo.processInfo.launchContext = context
        }

        private fun buildSystemProperties(): Dictionary<String, String> {
            var dict: Dictionary<String, String> = dictionaryOf()
            // The system properties contains the System environment (which, on Android, doesn't contain much of interest),
            for ((key, value) in System.getenv()) {
                dict[key] = value
            }

            // as well as the Java System.getProperties()
            // there are only a few system properties on the Android emulator: java.io.tmpdir, user.home, and http.agent "Dalvik/2.1.0 (Linux; U; Android 13; sdk_gphone64_arm64 Build/ TE1A.220922.021)"
            for ((key, value) in System.getProperties()) {
                dict[key.toString()] = value.toString()
            }

            // there are more system properties than are listed in the getProperties() keys, so also fetch sepcific individual known property keys
            for (key in arrayOf(
                "os.version",
                "java.vendor",
                "java.version",
                "user.home",
                "user.name",
                "file.separator",
                "line.separator",
                "java.class.path",
                "java.library.path",
                "java.class.version",
                "java.vm.name",
                "java.vm.version",
                "java.vm.vendor",
                "java.ext.dirs",
                "java.io.tmpdir",
                "java.specification.version",
                "java.specification.vendor",
                "java.specification.name",
                "java.home",
                "user.dir"
            )) {
                dict[key] = System.getProperty(key)
            }

            // and finally add in some android build constants so clients have a Foundation-compatible way of testing for the Android build number, ec.

            dict["android.os.Build.BOARD"] = android.os.Build.BOARD // The name of the underlying board, like "goldfish".
            dict["android.os.Build.BOOTLOADER"] = android.os.Build.BOOTLOADER // The system bootloader version number.
            dict["android.os.Build.BRAND"] = android.os.Build.BRAND // The consumer-visible brand with which the product/hardware will be associated, if any.
            dict["android.os.Build.DEVICE"] = android.os.Build.DEVICE // The name of the industrial design.
            dict["android.os.Build.DISPLAY"] = android.os.Build.DISPLAY // A build ID string meant for displaying to the user
            dict["android.os.Build.FINGERPRINT"] = android.os.Build.FINGERPRINT // A string that uniquely identifies this build.
            dict["android.os.Build.HARDWARE"] = android.os.Build.HARDWARE // The name of the hardware (from the kernel command line or /proc).
            dict["android.os.Build.HOST"] = android.os.Build.HOST // A string that uniquely identifies this build.
            dict["android.os.Build.ID"] = android.os.Build.ID // Either a changelist number, or a label like "M4-rc20".
            dict["android.os.Build.MANUFACTURER"] = android.os.Build.MANUFACTURER // The manufacturer of the product/hardware.
            dict["android.os.Build.MODEL"] = android.os.Build.MODEL // The end-user-visible name for the end product.
            //dict["android.os.Build.ODM_SKU"] = android.os.Build.ODM_SKU // The SKU of the device as set by the original design manufacturer (ODM). // API 31: java.lang.NoSuchFieldError: ODM_SKU
            dict["android.os.Build.PRODUCT"] = android.os.Build.PRODUCT // The name of the overall product.
            //dict["android.os.Build.SKU"] = android.os.Build.SKU // The SKU of the hardware (from the kernel command line). // API 31: java.lang.NoSuchFieldError: SKU
            //dict["android.os.Build.SOC_MANUFACTURER"] = android.os.Build.SOC_MANUFACTURER // The manufacturer of the device's primary system-on-chip. // API 31: java.lang.NoSuchFieldError: SOC_MANUFACTURER
            //dict["android.os.Build.SOC_MODEL"] = android.os.Build.SOC_MODEL // The model name of the device's primary system-on-chip. // API 31
            dict["android.os.Build.TAGS"] = android.os.Build.TAGS // Comma-separated tags describing the build, like "unsigned,debug".
            dict["android.os.Build.TYPE"] = android.os.Build.TYPE // The type of build, like "user" or "eng".
            dict["android.os.Build.USER"] = android.os.Build.USER // The user


            dict["android.os.Build.TIME"] = android.os.Build.TIME.toString() //  The time at which the build was produced, given in milliseconds since the UNIX epoch.

            //        dict["android.os.Build.SUPPORTED_32_BIT_ABIS"] = android.os.Build.SUPPORTED_32_BIT_ABIS.joinToString(",") // An ordered list of 32 bit ABIs supported by this device.
            //        dict["android.os.Build.SUPPORTED_64_BIT_ABIS"] = android.os.Build.SUPPORTED_64_BIT_ABIS.joinToString(",") // An ordered list of 64 bit ABIs supported by this device.
            //        dict["android.os.Build.SUPPORTED_ABIS"] = android.os.Build.SUPPORTED_ABIS.joinToString(",") // An ordered list of ABIs supported by this device.

            dict["android.os.Build.VERSION.BASE_OS"] = android.os.Build.VERSION.BASE_OS // The base OS build the product is based on.
            dict["android.os.Build.VERSION.CODENAME"] = android.os.Build.VERSION.CODENAME // The current development codename, or the string "REL" if this is a release build.
            dict["android.os.Build.VERSION.INCREMENTAL"] = android.os.Build.VERSION.INCREMENTAL // The internal value used by the underlying source control to represent this build. E.g., a perforce changelist number or a git hash.
            dict["android.os.Build.VERSION.PREVIEW_SDK_INT"] = android.os.Build.VERSION.PREVIEW_SDK_INT.description // The developer preview revision of a prerelease SDK. This value will always be 0 on production platform builds/devices.
            dict["android.os.Build.VERSION.RELEASE"] = android.os.Build.VERSION.RELEASE // The user-visible version string. E.g., "1.0" or "3.4b5" or "bananas". This field is an opaque string. Do not assume that its value has any particular structure or that values of RELEASE from different releases can be somehow ordered.
            dict["android.os.Build.VERSION.SDK_INT"] = android.os.Build.VERSION.SDK_INT.description // The SDK version of the software currently running on this hardware device. This value never changes while a device is booted, but it may increase when the hardware manufacturer provides an OTA update.
            dict["android.os.Build.VERSION.SECURITY_PATCH"] = android.os.Build.VERSION.SECURITY_PATCH // The user-visible security patch level. This value represents the date when the device most recently applied a security patch.

            return dict.sref()
        }
    }
}
