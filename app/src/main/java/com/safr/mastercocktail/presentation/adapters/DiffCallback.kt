package com.safr.mastercocktail.presentation.adapters


abstract class DiffCallback<Value> {
    abstract fun areItemsTheSame(
        oldItem: Value,
        newItem: Value
    ): Boolean

    abstract fun areContentsTheSame(
        oldItem: Value,
        newItem: Value
    ): Boolean

    fun getChangePayload(oldItem: Value, newItem: Value): Any? {
        return null
    }
}