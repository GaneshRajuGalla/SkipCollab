package skip.collab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.lib.*
import skip.lib.Array
import skip.model.*

import skip.foundation.*
import skip.ui.*
import android.Manifest
import android.app.Application
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

internal val logger: SkipLogger = SkipLogger(subsystem = "com.ganeshrajugalla", category = "SkipCollab")

/// The Android SDK number we are running against, or `nil` if not running on Android
internal val androidSDK = ProcessInfo.processInfo.environment["android.os.Build.VERSION.SDK_INT"].optionalflatMap({ it -> Int(it) })

/// The shared top-level view for the app, loaded from the platform-specific App delegates below.
///
/// The default implementation merely loads the `ContentView` for the app and logs a message.
internal class RootView: View {
    override fun body(): View {
        return ComposeView { composectx: ComposeContext ->
            ContentView()
                .task { MainActor.run {
                    logger.log("Welcome to Skip on ${if (androidSDK != null) "Android" else "Darwin"}!")
                    logger.warning("Skip app logs are viewable in the Xcode console for iOS; Android logs can be viewed in Studio or using adb logcat")
                } }.Compose(composectx)
        }
    }
}


/// AndroidAppMain is the `android.app.Application` entry point, and must match `application android:name` in the AndroidMainfest.xml file.
open class AndroidAppMain: Application {
    constructor() {
    }

    override fun onCreate() {
        super.onCreate()
        logger.info("starting app")
        ProcessInfo.launch(applicationContext)
    }

    companion object {
    }
}

/// AndroidAppMain is initial `androidx.appcompat.app.AppCompatActivity`, and must match `activity android:name` in the AndroidMainfest.xml file.
open class MainActivity: AppCompatActivity {
    constructor() {
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val saveableStateHolder = rememberSaveableStateHolder()
            saveableStateHolder.SaveableStateProvider(true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { MaterialThemedRootView() }
            }
        }

        // Example of requesting permissions on startup.
        // These must match the permissions in the AndroidManifest.xml file.
        //let permissions = listOf(
        //    Manifest.permission.ACCESS_COARSE_LOCATION,
        //    Manifest.permission.ACCESS_FINE_LOCATION
        //    Manifest.permission.CAMERA,
        //    Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //)
        //let requestTag = 1
        //ActivityCompat.requestPermissions(self, permissions.toTypedArray(), requestTag)
    }

    override fun onSaveInstanceState(bundle: android.os.Bundle): Unit = super.onSaveInstanceState(bundle)

    override fun onRestoreInstanceState(bundle: android.os.Bundle) {
        // Usually you restore your state in onCreate(). It is possible to restore it in onRestoreInstanceState() as well, but not very common. (onRestoreInstanceState() is called after onStart(), whereas onCreate() is called before onStart().
        logger.info("onRestoreInstanceState")
        super.onRestoreInstanceState(bundle)
    }

    override fun onRestart() {
        logger.info("onRestart")
        super.onRestart()
    }

    override fun onStart() {
        logger.info("onStart")
        super.onStart()
    }

    override fun onResume() {
        logger.info("onResume")
        super.onResume()
    }

    override fun onPause() {
        logger.info("onPause")
        super.onPause()
    }

    override fun onStop() {
        logger.info("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logger.info("onDestroy")
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: kotlin.Array<String>, grantResults: IntArray): Unit = logger.info("onRequestPermissionsResult: ${requestCode}")

    companion object {
    }
}

@Composable
internal fun MaterialThemedRootView() {
    val context = LocalContext.current.sref()
    val darkMode = isSystemInDarkTheme()
    // Dynamic color is available on Android 12+
    val dynamicColor = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S

    val colorScheme = if (dynamicColor) (if (darkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)) else (if (darkMode) darkColorScheme() else lightColorScheme())

    MaterialTheme(colorScheme = colorScheme) { RootView().Compose() }
}

