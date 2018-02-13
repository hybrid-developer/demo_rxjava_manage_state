package com.elyeproj.rxstate.presenter

import com.elyeproj.rxstate.model.DataModel
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay


object DataSource {

    enum class FetchStyle {
        FETCH_SUCCESS,
        FETCH_EMPTY,
        FETCH_ERROR
    }

    var style = FetchStyle.FETCH_ERROR

    suspend fun loadData(): Deferred<DataModel<String?>> {
        delay(5000)

        return async(UI) {  when (style) {
            FetchStyle.FETCH_SUCCESS -> DataModel.Ok("Data Loaded")
            FetchStyle.FETCH_EMPTY -> DataModel.Ok(null)
            FetchStyle.FETCH_ERROR -> DataModel.Exception(IllegalStateException("Error Fetching"))
        }
        }
    }
}