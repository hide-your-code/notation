package com.example.features.welcome.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.common.view.navigation.EntryProviderInstaller
import com.example.core.common.view.navigation.Navigator
import com.example.features.welcome.api.WelcomeKey
import com.example.features.welcome.impl.view.WelcomeScreen
import com.example.features.welcome.impl.view.vm.WelcomeVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object WelcomeModule {
    @IntoSet
    @Provides
    fun providesEntryProviderInstaller(
        navigator: Navigator,
    ): EntryProviderInstaller = {
        entry<WelcomeKey> { key ->
            val viewModel = hiltViewModel<WelcomeVM, WelcomeVM.Factory>(
                // Note: We need a new ViewModel for every new RouteB instance. Usually
                // we would need to supply a `key` String that is unique to the
                // instance, however, the ViewModelStoreNavEntryDecorator (supplied
                // above) does this for us, using `NavEntry.contentKey` to uniquely
                // identify the viewModel.
                //
                // tl;dr: Make sure you use rememberViewModelStoreNavEntryDecorator()
                // if you want a new ViewModel for each new navigation key instance.
                creationCallback = { factory ->
                    factory.create(key)
                }
            )

            WelcomeScreen(
                viewModel = viewModel,
                navigator = navigator,
            )
        }
    }
}
