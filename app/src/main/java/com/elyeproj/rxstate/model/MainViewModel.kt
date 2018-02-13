package com.elyeproj.rxstate.model

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.elyeproj.rxstate.presenter.DataSource
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel


class MainViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    private val stateChangeChannel = ConflatedBroadcastChannel<UiStateModel>()

    init {
        /** When the model is initialized we immediately start fetching data */
        fetchData()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared() called")
        stateChangeChannel.close()
    }

    fun connect(): SubscriptionReceiveChannel<UiStateModel> {
        return stateChangeChannel.openSubscription()
    }

    fun fetchData() = async {
        stateChangeChannel.send(UiStateModel.Loading())
        try {
            val state = DataSource.loadData().await()
            stateChangeChannel.send(UiStateModel.from(state))

        } catch (e: Exception) {
            Log.e("MainModel", "Exception happened when sending new state to channel: ${e.cause}")
        }
    }

    internal fun loadStyle(style: DataSource.FetchStyle) {
        DataSource.style = style
        fetchData()
    }
}