package com.elyeproj.rxstate.presenter

import android.util.Log
import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.model.UiStateModel.*
import com.elyeproj.rxstate.view.MainView

class MainPresenter(val view: MainView) {

    fun loadSuccess(model: MainViewModel) {
        Log.d("MainPresenter", "Load success clicked")
        model.loadSuccess()
    }

    fun loadError(model: MainViewModel) {
        Log.d("MainPresenter", "Load error clicked")
        model.loadError()
    }

    fun loadEmpty(model: MainViewModel) {
        Log.d("MainPresenter", "Load empty clicked")
        model.loadEmpty()
    }

    fun loadView(uiState: UiStateModel) {
        when(uiState) {
            is Loading -> view.isLoading()
            is Error -> view.isError(uiState.exception.localizedMessage)
            is Success -> when {
                uiState.result.dataString != null -> view.isSuccess(uiState.result)
                else -> view.isEmpty()
            }
        }
    }
}