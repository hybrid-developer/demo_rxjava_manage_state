package com.elyeproj.rxstate.presenter

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.model.UiStateModel.*
import com.elyeproj.rxstate.view.MainView
import kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach


class MainPresenter(val view: MainView) {

    private lateinit var subscription: SubscriptionReceiveChannel<UiStateModel>

    fun loadSuccess(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_SUCCESS)
    }

    fun loadError(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_ERROR)
    }

    fun loadEmpty(model: MainViewModel) {
        model.loadStyle(DataSource.FetchStyle.FETCH_EMPTY)
    }

    suspend fun subscribe(model: MainViewModel) {
        subscription = model.connect()
        subscription.subscribe { loadView(it) }
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

    fun unSubscribe() {
        subscription.close()
    }
}

inline suspend fun <E> SubscriptionReceiveChannel<E>.subscribe(action: (E) -> Unit) = consumeEach { action(it) }