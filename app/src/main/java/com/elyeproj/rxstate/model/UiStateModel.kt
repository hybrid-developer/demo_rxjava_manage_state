package com.elyeproj.rxstate.model

sealed class UiStateModel {

    companion object {
        fun <T> from(result: T) : UiStateModel {
            when (result) {
                is Loading -> return Loading()
                is DataModel.Ok<*> -> return Success(result.value)
                is DataModel.Exception -> return Error(result.exception)
            }
            return Error(IllegalArgumentException("Invalid Response"))
        }
    }

    data class Loading(val loadingMessage: String = "Loading...") : UiStateModel()
    data class Error(val exception: Throwable) : UiStateModel()
    data class Success(val result: String?) : UiStateModel()
}