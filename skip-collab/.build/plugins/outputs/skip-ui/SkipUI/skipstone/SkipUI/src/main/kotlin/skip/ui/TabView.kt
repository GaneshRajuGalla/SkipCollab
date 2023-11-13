// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class TabView<Content>: View where Content: View {
    internal val content: Content

    constructor(content: () -> Content) {
        this.content = content()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(selection: Binding<Any>?, content: () -> Content) {
        this.content = content()
    }

    @ExperimentalMaterial3Api
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        // Use a custom composer to count the number of child views in our content
        var tabCount = 0
        content.Compose(context = context.content(composer = ClosureComposer { _, _ -> tabCount += 1 }))

        val navController = rememberNavController()
        ComposeContainer(modifier = context.modifier, fillWidth = true, fillHeight = true) { modifier ->
            Scaffold(modifier = modifier, bottomBar = {
                NavigationBar(modifier = Modifier.fillMaxWidth()) {
                    for (tabIndex in 0 until tabCount) {
                        // Use a custom composer to get the tabIndex'th tab item
                        var composeIndex = 0
                        var tabItem: TabItem? = null
                        content.Compose(context = context.content(composer = ClosureComposer { view, _ ->
                            if (composeIndex == tabIndex) {
                                tabItem = view.strippingModifiers { it -> it as? TabItem }
                            }
                            composeIndex += 1
                        }))

                        // Render it
                        val tabItemContext = context.content()
                        NavigationBarItem(icon = { tabItem?.ComposeImage(context = tabItemContext) }, label = { tabItem?.ComposeTitle(context = tabItemContext) }, selected = String(describing = tabIndex) == currentRoute(for_ = navController), onClick = {
                            navController.navigate(String(describing = tabIndex)) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                // Avoid multiple copies of the same destination when reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        })
                    }
                }
            }) { padding ->
                NavHost(navController, startDestination = "0", enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None }) {
                    // Use a constant number of routes. Changing routes causes a NavHost to reset its state
                    for (tabIndex in 0 until 100) {
                        composable(String(describing = tabIndex)) {
                            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                // Use a custom composer to only render the tabIndex'th view
                                content.Compose(context = context.content(composer = TabIndexComposer(index = tabIndex)))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun currentRoute(for_: NavHostController): String? {
        val navController = for_
        // In your BottomNavigation composable, get the current NavBackStackEntry using the currentBackStackEntryAsState() function. This entry gives you access to the current NavDestination. The selected state of each BottomNavigationItem can then be determined by comparing the item's route with the route of the current destination and its parent destinations (to handle cases when you are using nested navigation) via the NavDestination hierarchy.
        return navController.currentBackStackEntryAsState().value?.destination?.route
    }

    companion object {
    }
}

internal class TabItem: View {
    internal val view: View
    internal val label: View

    internal constructor(view: View, label: () -> View) {
        // Don't copy view
        this.view = view
        this.label = label()
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        view.Compose(context = context)
    }

    @Composable
    internal fun ComposeTitle(context: ComposeContext) {
        label.Compose(context = context.content(composer = ClosureComposer { view, context ->
            val stripped = view.strippingModifiers { it -> it }
            val matchtarget_0 = stripped as? Label
            if (matchtarget_0 != null) {
                val label = matchtarget_0
                label.ComposeTitle(context = context(false))
            } else if (stripped is Text) {
                view.ComposeContent(context = context(false))
            }
        }))
    }

    @Composable
    internal fun ComposeImage(context: ComposeContext) {
        label.Compose(context = context.content(composer = ClosureComposer { view, context ->
            val stripped = view.strippingModifiers { it -> it }
            val matchtarget_1 = stripped as? Label
            if (matchtarget_1 != null) {
                val label = matchtarget_1
                label.ComposeImage(context = context(false))
            } else if (stripped is Image) {
                view.Compose(context = context(false))
            }
        }))
    }
}

internal open class TabIndexComposer: Composer {
    internal val index: Int
    internal open var currentIndex = 0

    internal constructor(index: Int) {
        this.index = index
    }

    override fun willCompose() {
        currentIndex = 0
    }

    @Composable
    override fun Compose(view: View, context: (Boolean) -> ComposeContext) {
        if (currentIndex == index) {
            view.ComposeContent(context = context(false))
        }
        currentIndex += 1
    }
}

// Model `TabViewStyle` as a struct. Kotlin does not support static members of protocols
class TabViewStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TabViewStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = TabViewStyle(rawValue = 0)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val page = TabViewStyle(rawValue = 1)
    }
}

