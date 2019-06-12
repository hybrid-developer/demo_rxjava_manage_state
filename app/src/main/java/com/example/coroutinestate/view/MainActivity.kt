package com.example.coroutinestate.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.coroutinestate.R
import com.example.coroutinestate.model.MainViewModel
import com.example.coroutinestate.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.btn_load_empty
import kotlinx.android.synthetic.main.activity_main.btn_load_error
import kotlinx.android.synthetic.main.activity_main.btn_load_success
import kotlinx.android.synthetic.main.activity_main.status_view

class MainActivity : AppCompatActivity(), MainView {

    private val mainPresenter by lazy { MainPresenter(this) }
    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.getViewState().observe { it?.let(mainPresenter::subscribe) }

        if (savedInstanceState == null) mainPresenter.loadView(model)

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
        status_view.isLoading("I'm loading...")
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