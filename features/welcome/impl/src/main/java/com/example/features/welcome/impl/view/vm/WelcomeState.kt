package com.example.features.welcome.impl.view.vm

import androidx.compose.runtime.Stable
import com.example.core.common.view.viewmodel.ViewState

@Stable
data class WelcomeState(
    val isLoading: Boolean = true,
) : ViewState
