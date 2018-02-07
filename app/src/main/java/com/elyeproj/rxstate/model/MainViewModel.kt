package com.elyeproj.rxstate.model

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.elyeproj.rxstate.presenter.DataSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainViewModel : ViewModel() {

    private var disposableContainer: CompositeDisposable = CompositeDisposable()

    private val dataSource = DataSource()

    val fetcher: Observable<UiStateModel> = dataSource.relay.replay(1).autoConnect()
        .subscribeOn(Schedulers.computation())
        .map { f -> loadData(f) }
        .map { result -> UiStateModel.from(result) }
        .startWith(UiStateModel.Loading())
        .onErrorReturn { exception -> UiStateModel.Error(exception) }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { Log.d("MainModel", "Fetcher had onNext called: "  + it.toString()) }

    override fun onCleared() {
        super.onCleared()
        disposableContainer.dispose()
    }

    fun loadSuccess() {
        Log.d("MainModel", "Load Success value.")
        dataSource.relay.accept(DataSource.FetchStyle.FETCH_SUCCESS)
    }

    fun loadEmpty() {
        Log.d("MainModel", "Load Empty value")
        dataSource.relay.accept(DataSource.FetchStyle.FETCH_EMPTY)
    }

    fun loadError() {
        Log.d("MainModel", "Load Error value")
        dataSource.relay.accept(DataSource.FetchStyle.FETCH_ERROR)

    }

    private fun loadData(f: DataSource.FetchStyle): DataModel {
        Thread.sleep(5000)

        return when (f) {
            DataSource.FetchStyle.FETCH_SUCCESS -> DataModel("Data Loaded")
            DataSource.FetchStyle.FETCH_EMPTY -> DataModel(null)
            DataSource.FetchStyle.FETCH_ERROR -> throw IllegalStateException("Error Fetching")
        }
    }
}