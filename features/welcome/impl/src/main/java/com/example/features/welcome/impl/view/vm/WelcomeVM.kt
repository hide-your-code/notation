package com.example.features.welcome.impl.view.vm

import com.example.core.common.view.viewmodel.CommonVM
import com.example.features.welcome.api.WelcomeKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = WelcomeVM.Factory::class)
class WelcomeVM @AssistedInject constructor(
    @Assisted private val navKey: WelcomeKey,
) : CommonVM<WelcomeState, WelcomeAction, WelcomeEvent>() {
    override fun initState(): WelcomeState {
        return WelcomeState()
    }

    override fun handleAction(action: WelcomeAction) {}

    @AssistedFactory
    interface Factory {
        fun create(navKey: WelcomeKey): WelcomeVM
    }
}
