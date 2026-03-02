package com.example.notation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.core.common.view.navigation.EntryProviderInstaller
import com.example.core.common.view.navigation.Navigator
import com.example.core.common.view.result.event.LocalResultEventBus
import com.example.core.common.view.result.event.ResultEventBus
import com.example.notation.ui.theme.NotationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotationActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderScope: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val resultEventBus = remember { ResultEventBus() }

            NotationTheme {
                CompositionLocalProvider(LocalResultEventBus provides resultEventBus) {
                    NavDisplay(
                        backStack = navigator.backStack,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        entryProvider = entryProvider {
                            entryProviderScope.forEach { builder -> this.builder() }
                        },
                    )
                }
            }
        }
    }
}
