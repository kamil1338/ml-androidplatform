package com.github.kamil1338.activity_recognition_core.core.util

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData

fun <T : Any> mutableLiveData(initialValue: T? = null) = NonNullMutableLiveData<T>().apply {
    initialValue?.let {
        value = initialValue
    }
}

fun <T : Any?> nullableMutableLiveData(initialValue: T? = null) = MutableLiveData<T>().apply {
    initialValue?.let {
        value = initialValue
    }
}

class NonNullMutableLiveData<T : Any> internal constructor() : MutableLiveData<T>() {

    @NonNull
    override fun getValue(): T {
        return super.getValue()!!
    }

    @MainThread
    fun observeForeverNonNull(observer: (T) -> Unit) {
        observeForever { observer(it!!) }
    }
}