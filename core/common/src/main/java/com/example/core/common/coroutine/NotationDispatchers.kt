package com.example.core.common.coroutine

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: NotationDispatchers)

enum class NotationDispatchers {
    Default,
    IO,
}
