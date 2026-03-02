package com.example.core.common.view.result.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

/**
 * Local for receiving results in a [ResultEventBus]
 */
object LocalResultEventBus {
    private val LocalResultEventBus: ProvidableCompositionLocal<ResultEventBus?> =
        compositionLocalOf { null }

    /**
     * The current [ResultEventBus]
     */
    val current: ResultEventBus
        @Composable
        get() = LocalResultEventBus.current ?: error("No ResultEventBus has been provided")

    /**
     * Provides a [ResultEventBus] to the composition
     */
    infix fun provides(
        bus: ResultEventBus
    ): ProvidedValue<ResultEventBus?> {
        return LocalResultEventBus.provides(bus)
    }
}
