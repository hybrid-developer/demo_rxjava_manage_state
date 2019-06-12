package com.example.coroutinestate.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinestate.presenter.DataSource
import kotlinx.coroutines.launch

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
        publish(Loading())
        try {
            viewModelScope.launch {
                publish(UiStateModel.from(DataSource.loadData()))
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