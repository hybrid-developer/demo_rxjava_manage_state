package com.elyeproj.rxstate.view

import com.elyeproj.rxstate.model.DataModel

interface MainView {
    fun isEmpty()
    fun isLoading()
    fun isSuccess(data: String)
    fun isError(errorMessage: String)
}