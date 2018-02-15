package com.elyeproj.rxstate.presenter

import com.elyeproj.rxstate.model.DataModel
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay


object DataSource {

    enum class FetchStyle {
        FETCH_SUCCESS,
        FETCH_EMPTY,
        FETCH_ERROR
    }

    var style = FetchStyle.FETCH_ERROR

    fun loadData() = async {
        delay(5000)

        when (style) {
            FetchStyle.FETCH_SUCCESS -> DataModel.Ok("Data Loaded")
            FetchStyle.FETCH_EMPTY -> DataModel.Ok(null)
            FetchStyle.FETCH_ERROR -> DataModel.Exception(IllegalStateException("Error Fetching"))
        }
    }
}