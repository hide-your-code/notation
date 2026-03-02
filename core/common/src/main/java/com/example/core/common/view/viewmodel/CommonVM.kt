package com.example.core.common.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class CommonVM<S : ViewState, A : ViewAction, E : ViewEvent> : ViewModel() {
    abstract fun initState(): S

    protected val _state = MutableStateFlow(initState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _action = MutableSharedFlow<A>(
        replay = 0,
        extraBufferCapacity = 64,
    )
    val action: SharedFlow<A> = _action.asSharedFlow()

    protected val _event = Channel<E>(capacity = Channel.BUFFERED)
    val event: Flow<E> = _event.receiveAsFlow()

    init {
        Timber.d("Initialized!")
        viewModelScope.launch {
            action.collect(::handleAction)
        }
    }

    fun sendAction(action: A) {
        _action.tryEmit(action)
    }

    protected abstract fun handleAction(action: A)
}
