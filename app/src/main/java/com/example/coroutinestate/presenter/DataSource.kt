package com.example.coroutinestate.presenter

import com.example.coroutinestate.model.DataModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


object DataSource {

    enum class FetchStyle {
        FETCH_SUCCESS,
        FETCH_EMPTY,
        FETCH_ERROR
    }

    var style = FetchStyle.FETCH_ERROR

    suspend fun loadData() = coroutineScope {
        delay(5000)

        when (style) {
            FetchStyle.FETCH_SUCCESS -> DataModel.Ok("Data Loaded")
            FetchStyle.FETCH_EMPTY -> DataModel.Ok(null)
            FetchStyle.FETCH_ERROR -> DataModel.Exception(IllegalStateException("Error Fetching"))
        }
    }
}