package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.ui.UiText

sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: UiText? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(uiText: UiText, data: T? = null) : Resource<T>(data, uiText)
}