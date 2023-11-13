package skip.collab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.foundation.*
import skip.lib.*
import skip.model.*

import skip.ui.*

internal class ContentView: View {
    internal var setting: Boolean
        get() = _setting.wrappedValue
        set(newValue) {
            _setting.wrappedValue = newValue
        }
    internal var _setting: skip.ui.AppStorage<Boolean>

    override fun body(): View {
        return ComposeView { composectx: ComposeContext ->
            TabView {
                ComposeView { composectx: ComposeContext ->
                    VStack {
                        ComposeView { composectx: ComposeContext ->
                            Text("Welcome Skipper!").Compose(composectx)
                            Image(systemName = "heart.fill")
                                .foregroundColor(Color.red).Compose(composectx)
                            ComposeResult.ok
                        }
                    }
                    .font(Font.largeTitle)
                    .tabItem {
                        ComposeView { composectx: ComposeContext ->
                            Label("Welcome", systemImage = "heart.fill").Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)

                    NavigationStack {
                        ComposeView { composectx: ComposeContext ->
                            List {
                                ComposeView { composectx: ComposeContext ->
                                    ForEach(1 until 1_000) { i ->
                                        ComposeView { composectx: ComposeContext ->
                                            NavigationLink("Home ${i}", value = i).Compose(composectx)
                                            ComposeResult.ok
                                        }
                                    }.Compose(composectx)
                                    ComposeResult.ok
                                }
                            }
                            .navigationTitle("Navigation")
                            .navigationDestination(for_ = Int::class) { i ->
                                ComposeView { composectx: ComposeContext ->
                                    Text("Destination ${i}")
                                        .font(Font.title)
                                        .navigationTitle("Navigation ${i}").Compose(composectx)
                                    ComposeResult.ok
                                }
                            }.Compose(composectx)
                            ComposeResult.ok
                        }
                    }
                    .tabItem {
                        ComposeView { composectx: ComposeContext ->
                            Label("Home", systemImage = "house.fill").Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)

                    Form {
                        ComposeView { composectx: ComposeContext ->
                            Text("Settings")
                                .font(Font.largeTitle).Compose(composectx)
                            Toggle("Option", isOn = Binding({ setting }, { it -> setting = it })).Compose(composectx)
                            ComposeResult.ok
                        }
                    }
                    .tabItem {
                        ComposeView { composectx: ComposeContext ->
                            Label("Settings", systemImage = "gearshape.fill").Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
                    ComposeResult.ok
                }
            }.Compose(composectx)
        }
    }

    @Composable
    override fun ComposeContent(composectx: ComposeContext) {
        val initialsetting = _setting.wrappedValue
        var composesetting by remember { mutableStateOf(initialsetting) }
        _setting.sync(composesetting, { composesetting = it })

        body().Compose(composectx)
    }

    constructor(setting: Boolean = true) {
        this._setting = skip.ui.AppStorage(wrappedValue = setting, "setting")
    }
}
