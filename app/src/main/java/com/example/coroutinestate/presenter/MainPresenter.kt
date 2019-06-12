package com.example.coroutinestate.presenter

import com.example.coroutinestate.model.Error
import com.example.coroutinestate.model.Error.HttpError
import com.example.coroutinestate.model.Error.NotConnectedError
import com.example.coroutinestate.model.Error.PreferenceNotEnabledError
import com.example.coroutinestate.model.Loading
import com.example.coroutinestate.model.MainViewModel
import com.example.coroutinestate.model.Success
import com.example.coroutinestate.model.UiStateModel
import com.example.coroutinestate.view.MainView


class MainPresenter(val view: MainView, private val connected: Boolean = true) {

    fun loadSuccess(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_SUCCESS)
        loadView(model)
    }

    fun loadError(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_ERROR)
        loadView(model)
    }

    fun loadEmpty(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_EMPTY)
        loadView(model)
    }

    /**
     * Renders the view based on what [uiState] we're in.
     * @param uiState the state that the presenter should have the view display
     */
    fun subscribe(uiState: UiStateModel) = when(uiState) {
        is Loading -> view.isLoading()
        is Error -> when(uiState) {
            is NotConnectedError -> { view.isError(uiState.message) }
            is HttpError -> { view.isError(uiState.message) }
            is PreferenceNotEnabledError -> { view.isError(uiState.message) }
        }
        is Success -> when {
            uiState.result != null -> view.isSuccess(uiState.result)
            else -> view.isEmpty()
        }
    }

    fun loadView(model: MainViewModel) {
        if (!connected) {
            model.publish(NotConnectedError("Not connected"))
        } else {
            model.fetchData()
        }
    }
}