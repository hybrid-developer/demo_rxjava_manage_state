package com.example.coroutinestate.model


sealed class DataModel<out T : Any?> {
    /**
     * Successful result of request without errors
     */
    class Ok<out T : String?>(
         val value: T?
    ) : DataModel<T>() {
        override fun toString() = "Datamodel.Ok{value=$value}"
    }

    /**
     * Network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response
     */
    class Exception(
        val exception: Throwable
    ) : DataModel<Nothing>() {
        override fun toString() = "Datamodel.Exception{$exception}"
    }

}

