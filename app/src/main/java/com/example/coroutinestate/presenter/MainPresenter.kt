package com.example.coroutinestate.presenter

import com.example.coroutinestate.model.MainViewModel
import com.example.coroutinestate.model.UiStateModel
import com.example.coroutinestate.model.UiStateModel.*
import com.example.coroutinestate.view.MainView


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

    fun subscribe(uiState: UiStateModel) {
        when(uiState) {
            is Loading -> view.isLoading()
            is Error -> view.isError(uiState.exception.localizedMessage)
            is Success -> when {
                uiState.result != null -> view.isSuccess(uiState.result)
                else -> view.isEmpty()
            }
        }
    }

    fun getData(model: MainViewModel) {
        model.fetchData()
    }
}