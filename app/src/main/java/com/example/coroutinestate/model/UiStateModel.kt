package com.example.coroutinestate.model

sealed class UiStateModel {

    companion object {
        fun <T> from(result: T) : UiStateModel = when (result) {
            is Loading -> Loading()
            is DataModel.Ok<*> -> Success(
                result.value
            )
            is DataModel.Exception -> Error.HttpError()
            else -> throw IllegalArgumentException("Invalid result passed as argument: $result")
        }
    }
}

data class Loading(val loadingMessage: String = "Loading...") : UiStateModel()
data class Success(val result: String?) : UiStateModel()
sealed class Error(exception: Throwable) : UiStateModel() {
    data class NotConnectedError(val message: String = "Not Connected") : Error(IllegalStateException(message))
    data class HttpError(val message: String = "Http Error") : Error(IllegalStateException(message))
    data class PreferenceNotEnabledError(val message: String = "Preference Error") : Error(IllegalStateException(message))
}