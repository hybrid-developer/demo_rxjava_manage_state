@file:Suppress("IllegalIdentifier")
package com.elyeproj.rxstate.presenter


import com.elyeproj.rxstate.model.DataModel
import com.elyeproj.rxstate.model.MainViewModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.view.MainView
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

class MainPresenterTest {

    @Mock
    private lateinit var view: MainView

    @Mock
    private lateinit var model: MainViewModel

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view)
    }

    @Test
    fun `When uistate is loading, view should show loading message`() = runBlocking {
        // Given
        UiStateModel.from(UiStateModel.Loading())

        // When
        presenter.subscribe(model)

        // Then
        verify(model).connect()
        verify(view, times(1)).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, never()).isError(any())    }

    @Test
    fun `When data was loaded succesful, view should show success`() = runBlocking {
        // Given
        val data = DataModel.Ok("Data Loaded")
        val uistate = UiStateModel.from(data)

        // When
        presenter.subscribe(model)

        // Then
        verify(view, never()).isLoading()
        verify(view, times(1)).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, never()).isError(any())
    }

    @Test
    fun `When no data was returned, view should show empty message`() = runBlocking {
        // Given
        val data = DataModel.Ok(null)
        val uistate = UiStateModel.from(data)

        // When
        presenter.subscribe(model)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, times(1)).isEmpty()
        verify(view, never()).isError(any())
    }

    @Test
    fun `When an error happened while loading, view should show error`() = runBlocking {
        // Given
        val throwable = DataModel.Exception(IllegalArgumentException("Invalid Response"))
        val uistate = UiStateModel.from(throwable)

        // When
        presenter.subscribe(model)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(any())
        verify(view, never()).isEmpty()
        verify(view, times(1)).isError(any())
    }
}