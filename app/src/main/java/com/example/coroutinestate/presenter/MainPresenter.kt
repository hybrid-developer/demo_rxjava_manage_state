package com.example.coroutinestate.presenter

import com.example.coroutinestate.model.MainViewModel
import com.example.coroutinestate.model.UiStateModel
import com.example.coroutinestate.model.UiStateModel.*
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
        is UiStateModel.Loading -> view.isLoading()
        is UiStateModel.Error -> when(uiState) {
            is UiStateModel.Error.NotConnectedError -> { view.isError(uiState.message) }
            is UiStateModel.Error.HttpError -> { view.isError(uiState.message) }
            is UiStateModel.Error.PreferenceNotEnabledError -> { view.isError(uiState.message) }
        }
        is UiStateModel.Success -> when {
            uiState.result != null -> view.isSuccess(uiState.result)
            else -> view.isEmpty()
        }
    }

    fun loadView(model: MainViewModel) {
        if (!connected) {
            model.publish(Error.NotConnectedError("Not connected"))
        } else {
            model.fetchData()
        }
    }
}