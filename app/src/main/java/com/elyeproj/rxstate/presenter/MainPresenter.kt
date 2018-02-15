package com.elyeproj.rxstate.presenter

import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.model.UiStateModel.*
import com.elyeproj.rxstate.view.MainView


class MainPresenter(val view: MainView) {

    fun loadSuccess(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_SUCCESS)
    }

    fun loadError(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_ERROR)
    }

    fun loadEmpty(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_EMPTY)
    }

    fun subscribe(state: UiStateModel) {
        loadView(state)
    }

    private fun loadView(uiState: UiStateModel) {
        when(uiState) {
            is Loading -> view.isLoading()
            is Error -> view.isError(uiState.exception.localizedMessage)
            is Success -> when {
                uiState.result != null -> view.isSuccess(uiState.result)
                else -> view.isEmpty()
            }
        }
    }
}

//inline suspend fun <E> SubscriptionReceiveChannel<E>.subscribe(action: (E) -> Unit) = consumeEach { action(it) }