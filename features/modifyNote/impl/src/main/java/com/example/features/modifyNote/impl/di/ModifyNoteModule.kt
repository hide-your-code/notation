package com.example.features.modifyNote.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.common.view.navigation.EntryProviderInstaller
import com.example.core.common.view.navigation.Navigator
import com.example.features.modifyNote.api.ModifyNoteKey
import com.example.features.modifyNote.impl.view.ModifyNoteScreen
import com.example.features.modifyNote.impl.view.vm.ModifyNoteVM
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
    fun provideEntryProviderInstaller(
        navigator: Navigator,
    ): EntryProviderInstaller = {
        entry<ModifyNoteKey> { key ->
            val viewModel = hiltViewModel<ModifyNoteVM, ModifyNoteVM.Factory>(
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

            ModifyNoteScreen(
                viewModel = viewModel,
                navigator = navigator,
            )
        }
    }
}
