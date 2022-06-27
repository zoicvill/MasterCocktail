package com.safr.mastercocktail.domain.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

abstract class FlowUseCase<T> {

    /**
     * Trigger for the action which can be done in this request
     */
    private val trigger = MutableStateFlow(true)

    /**
     * Exposes result of this use case
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val resultFlow: Flow<T> = trigger.flatMapLatest {
        performAction()
    }

    protected abstract suspend fun performAction() : Flow<T>

    /**
     * Triggers the execution of this use case
     */
    suspend fun launch() {
        trigger.emit(!(trigger.value))
    }
}