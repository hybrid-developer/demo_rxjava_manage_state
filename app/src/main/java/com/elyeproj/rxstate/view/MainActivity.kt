package com.elyeproj.rxstate.view

import android.arch.lifecycle.Observer
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

        model.getViewState().observe(this, Observer { value -> mainPresenter.subscribe(value!!) } )

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
    }

    override fun isEmpty() {
        status_view.isEmpty()
        enableButtons(true)
    }

    override fun isLoading() {
        status_view.isLoading()
        enableButtons(false)
    }

    override fun isSuccess(data: String) {
        status_view.isSuccess(data)
        enableButtons(true)
    }

    override fun isError(errorMessage: String) {
        status_view.isError(errorMessage)
        enableButtons(true)
    }

    private fun enableButtons(enable: Boolean) {
        btn_load_error.isEnabled = enable
        btn_load_success.isEnabled = enable
        btn_load_empty.isEnabled = enable
    }
}