package com.elyeproj.rxstate.model

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.elyeproj.rxstate.presenter.DataSource
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.launch


class MainViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    val stateChangeChannel = ConflatedBroadcastChannel<UiStateModel>()

    init {
        async { fetchData() }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared() called")
        stateChangeChannel.close()
    }

    suspend fun fetchData() {
        Log.d("MainModel", "Fetching data")
        Log.d("MainModel", "Sending laoding UiState...")
        stateChangeChannel.send(UiStateModel.Loading())
        try {
            Log.d("MainModel", "Retrieving Uistate from Datasource...")
            val state = DataSource.loadData().await()
            Log.d("MainModel", "State is: $state")
            Log.d("MainModel", "Sending Uistate to channel...")
            stateChangeChannel.send(UiStateModel.from(state))
            Log.d("MainModel", "Uistate in channel is " + stateChangeChannel.value)

        } catch (e: Exception) {
            Log.e("MainModel", "Exception happened when sending new state to channel: ${e.cause}")
        }
    }

    fun loadSuccess() {
        Log.d("MainModel", "Please set the datasource value to Success.")
        DataSource.style = DataSource.FetchStyle.FETCH_SUCCESS
        launch(UI) { fetchData() }
    }

    fun loadEmpty() {
        Log.d("MainModel", "Please set the datasource value to Empty")
        DataSource.style = DataSource.FetchStyle.FETCH_EMPTY
        launch(UI) { fetchData() }

    }

    fun loadError() {
        Log.d("MainModel", "Please set the datasource value to Error")
        DataSource.style = DataSource.FetchStyle.FETCH_ERROR
        launch(UI) { fetchData() }
    }
}