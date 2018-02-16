package com.example.coroutinestate.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.coroutinestate.R
import com.example.coroutinestate.model.MainViewModel
import com.example.coroutinestate.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private val mainPresenter by lazy { MainPresenter(this) }
    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.getViewState().observe { value -> mainPresenter.subscribe(value!!) }

        if (savedInstanceState == null) mainPresenter.getData(model)

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

    private fun <T> LiveData<T>.observe(observe: (T?) -> Unit)
            = observe(this@MainActivity, Observer { observe(it) })
}