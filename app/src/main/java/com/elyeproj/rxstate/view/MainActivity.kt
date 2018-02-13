package com.elyeproj.rxstate.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.elyeproj.rxstate.R
import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity(), MainView {

    private val mainPresenter by lazy { MainPresenter(this) }
    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(UI) {
            mainPresenter.subscribe(model)
        }

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
        Log.d("View", "onDestroy()")
        mainPresenter.subscription.close()
    }

    override fun isEmpty() {
        status_view.isEmpty()
    }

    override fun isLoading() {
        status_view.isLoading()
    }

    override fun isSuccess(data: String) {
        status_view.isSuccess(data)
    }

    override fun isError(errorMessage: String) {
        status_view.isError(errorMessage)
    }
}