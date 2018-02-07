package com.elyeproj.rxstate.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.elyeproj.rxstate.R
import com.elyeproj.rxstate.model.DataModel
import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.presenter.MainPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private val compositeDisposable = CompositeDisposable()

    private val mainPresenter by lazy {
        MainPresenter(this)
    }

    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        model.fetcher
            .doOnNext({ Log.d("MainActivity", "onNext trigered in the model")})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    uiState -> mainPresenter.loadView(uiState)
            }).addTo(compositeDisposable)


        btn_load_success.setOnClickListener {
            mainPresenter.loadSuccess(model)
        }

        btn_load_error.setOnClickListener {
            mainPresenter.loadError(model)
        }

        btn_load_empty.setOnClickListener {
            mainPresenter.loadEmpty(model)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun isEmpty() {
        status_view.isEmpty()
    }

    override fun isLoading() {
        status_view.isLoading()
    }

    override fun isSuccess(data: DataModel) {
        data.dataString?.let { status_view.isSuccess(it) }
    }

    override fun isError(errorMessage: String) {
        status_view.isError(errorMessage)
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}