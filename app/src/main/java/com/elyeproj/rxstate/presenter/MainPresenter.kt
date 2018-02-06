package com.elyeproj.rxstate.presenter

import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.model.UiStateModel.*
import com.elyeproj.rxstate.presenter.DataSource.FetchStyle.*
import com.elyeproj.rxstate.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(val view: MainView, val dataSource: DataSource) {

    private var disposable: Disposable? = null

    fun loadSuccess() {
        dataSource.fetchStyle = FETCH_SUCCESS
        loadData()
    }


    fun loadError() {
        dataSource.fetchStyle = FETCH_ERROR
        loadData()
    }

    fun loadEmpty() {
        dataSource.fetchStyle = FETCH_EMPTY
        loadData()
    }

    fun loadData() {
        disposable?.dispose()

        disposable = dataSource.fetchData()
                .map { result -> UiStateModel.from(result) }
                .onErrorReturn { exception -> Error(exception as Exception) }
                .startWith(Loading())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uiState ->
                    when(uiState) {
                        is Loading -> view.isLoading()
                        is Error -> view.isError(uiState.exception.localizedMessage)
                        is Success -> when {
                            uiState.result.dataString != null -> view.isSuccess(uiState.result)
                            else -> view.isEmpty()
                        }
                        else -> IllegalArgumentException("Invalid Response")
                    }
                })
    }

    fun destroy() {
        disposable?.dispose()
    }
}