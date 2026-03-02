package com.example.features.notes.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.common.view.navigation.EntryProviderInstaller
import com.example.core.common.view.navigation.Navigator
import com.example.features.notes.api.NotesPagingKey
import com.example.features.notes.impl.view.NotesScreen
import com.example.features.notes.impl.view.vm.NotesVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NotesModule {
    @IntoSet
    @Provides
    fun providesEntryProviderInstaller(
        navigator: Navigator,
    ): EntryProviderInstaller = {
        entry<NotesPagingKey> { key ->
            val viewModel = hiltViewModel<NotesVM, NotesVM.Factory>(
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

            NotesScreen(
                viewModel = viewModel,
                navigator = navigator,
            )
        }
    }
}
