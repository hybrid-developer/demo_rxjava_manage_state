@file:Suppress("IllegalIdentifier")
package com.elyeproj.rxstate.presenter


import com.elyeproj.rxstate.model.DataModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.view.MainView
import io.reactivex.Observable
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

class MainPresenterTest {

    @Mock
    lateinit var view: MainView

    lateinit var presenter: MainPresenter

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view)
    }

    @Test
    fun `When uistate is loading, view should show loading message`() {
        // Given
        val uistate = UiStateModel.from(UiStateModel.Loading())

        // When
        presenter.loadView(uistate)

        // Then
        verify(view, times(1)).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, never()).isError(any())    }

    @Test
    fun `When data was loaded succesful, view should show success`() {
        // Given
        val data = DataModel("Data Loaded")
        val uistate = UiStateModel.from(data)

        // When
        presenter.loadView(uistate)

        // Then
        verify(view, never()).isLoading()
        verify(view, times(1)).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, never()).isError(any())
    }

    @Test
    fun `When no data was returned, view should show empty message`() {
        // Given
        val data = DataModel(null)
        val uistate = UiStateModel.from(data)

        // When
        presenter.loadView(uistate)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, times(1)).isEmpty()
        verify(view, never()).isError(any())
    }

    @Test
    fun `When an error happened while loading, view should show error`() {
        // Given
        val throwable = IllegalArgumentException("Invalid Response")

        val uistate = UiStateModel.from(throwable)

        // When
        presenter.loadView(uistate)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, times(1)).isError(any())
    }
}