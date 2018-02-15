package com.elyeproj.rxstate.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.elyeproj.rxstate.presenter.DataSource
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch


/**
 * Class responsible for maintaining viewstate across configurationchanges
 */
class MainViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    private val viewState = UiStateLiveData()

    init {
        /** When the model is initialized we immediately start fetching data */
        fetchData()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared() called")
    }

    fun fetchData() {
        viewState.setUiState(UiStateModel.Loading())
        try {
            launch { DataSource.loadData().await() }
            viewState.setUiState(UiStateModel.from(DataSource.loadData()))
        } catch (e: Exception) {
            Log.e("MainModel", "Exception happened when sending new state to channel: ${e.cause}")
        }
    }

    fun getViewState() : LiveData<UiStateModel> {
        return viewState
    }

    internal fun loadStyle(style: DataSource.FetchStyle) {
        DataSource.style = style
        fetchData()
    }
}

/**
 * [LiveData] object for observing changes in viewstate. Uses kotlin coroutine [channel]s for
 *
 */
class UiStateLiveData : LiveData<UiStateModel>() {

    private val channel = ConflatedBroadcastChannel<UiStateModel>(UiStateModel.Error(IllegalArgumentException("Invalid Response")))
    private lateinit var subscription: SubscriptionReceiveChannel<UiStateModel>

    override fun onInactive() {
        subscription.close()
        super.onInactive()
    }

    override fun onActive() {
        super.onActive()
        subscription = channel.openSubscription()
        async { subscription.consumeEach { value -> postValue(value) } }
    }

    fun setUiState(state: UiStateModel) {
        async { channel.send(state) }
    }
}