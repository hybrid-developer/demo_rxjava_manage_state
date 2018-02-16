package com.example.coroutinestate.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.coroutinestate.presenter.DataSource
import kotlinx.coroutines.experimental.launch


/**
 * Viewmodel that holds the view state
 */
class MainViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    private val viewState = MutableLiveData<UiStateModel>()

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared() called")
    }

    fun fetchData() {
        publish(UiStateModel.Loading())
        try {
            launch {
                publish(UiStateModel.from(DataSource.loadData().await()))
            }
        } catch (e: Exception) {
            Log.e("MainModel", "Exception happened when sending new state to channel: ${e.cause}")
        }
    }

    fun getViewState(): LiveData<UiStateModel> {
        return viewState
    }

    internal fun loadStyle(style: DataSource.FetchStyle) {
        DataSource.style = style
    }

    fun publish(state: UiStateModel) {
        viewState.postValue(state)
    }
}