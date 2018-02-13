package com.elyeproj.rxstate.presenter

import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.model.UiStateModel.*
import com.elyeproj.rxstate.view.MainView
import kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach


class MainPresenter(val view: MainView) {

    lateinit var subscription: SubscriptionReceiveChannel<UiStateModel>

    fun loadSuccess(model: MainViewModel) {
        model.loadSuccess()
    }

    fun loadError(model: MainViewModel) {
        model.loadError()
    }

    fun loadEmpty(model: MainViewModel) {
        model.loadEmpty()
    }

    suspend fun subscribe(model: MainViewModel) {
        subscription = model.stateChangeChannel.openSubscription()
        subscription.consumeEach {
                value -> loadView(value)
        }
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