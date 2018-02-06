package com.elyeproj.rxstate.model

sealed class UiStateModel {

    companion object {
        fun <T> from(result: T) : UiStateModel {
            when (result) {
                is Loading -> return Loading()
                is DataModel -> return Success(result)
                is Exception -> return Error(result)
            }
            return Error(IllegalArgumentException("Invalid Response"))
        }
    }

    data class Loading(val loadingMessage: String = "Loading...") : UiStateModel()
    data class Error(val exception: Exception) : UiStateModel()
    data class Success(val result: DataModel) : UiStateModel()
}



