package com.example.features.notesManual.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.common.view.navigation.EntryProviderInstaller
import com.example.core.common.view.navigation.Navigator
import com.example.features.notesManual.api.NotesManualKey
import com.example.features.notesManual.impl.view.NotesManualScreen
import com.example.features.notesManual.impl.view.vm.NotesManualVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NotesManualModule {
    @IntoSet
    @Provides
    fun providesEntryProviderInstaller(
        navigator: Navigator,
    ): EntryProviderInstaller = {
        entry<NotesManualKey> { key ->
            val viewModel = hiltViewModel<NotesManualVM, NotesManualVM.Factory>(
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
                })

            NotesManualScreen(
                viewModel = viewModel,
                navigator = navigator,
            )
        }
    }
}
