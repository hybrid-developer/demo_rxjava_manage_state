package com.example.coroutinestate.view

interface MainView {
    fun isEmpty()
    fun isLoading()
    fun isSuccess(data: String)
    fun isError(errorMessage: String)
}