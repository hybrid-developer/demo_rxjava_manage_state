@file:Suppress("IllegalIdentifier")
package com.elyeproj.rxstate.presenter


import com.elyeproj.rxstate.model.DataModel
import com.elyeproj.rxstate.model.UiStateModel
import com.elyeproj.rxstate.view.MainView
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as whenever

class MainPresenterTest {

    private val view = mock<MainView>()

    private val presenter = MainPresenter(view)

    @Test
    fun `When uistate is loading, view should show loading message`() {
        // Given
        val state = UiStateModel.Loading()

        // When
        presenter.subscribe(state)

        // Then
        verify(view).isLoading()
        verify(view, never()).isSuccess(anyString())
        verify(view, never()).isEmpty()
        verify(view, never()).isError(anyString())
    }

    @Test
    fun `When data was loaded successful, view should show success`() {
        // Given
        val data = DataModel.Ok("Data Loaded")
        val state = UiStateModel.from(data)

        // When
        presenter.subscribe(state)

        // Then
        verify(view, never()).isLoading()
        verify(view, times(1)).isSuccess(data.value!!)
        verify(view, never()).isEmpty()
        verify(view, never()).isError(anyString())
    }

    @Test
    fun `When no data was returned, view should show empty message`() {
        // Given
        val data = DataModel.Ok(null)
        val state = UiStateModel.from(data)

        // When
        presenter.subscribe(state)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(anyString())
        verify(view, times(1)).isEmpty()
        verify(view, never()).isError(anyString())
    }

    @Test
    fun `When an error happened while loading, view should show error`() {
        // Given
        val throwable = DataModel.Exception(IllegalArgumentException("Invalid Response"))
        val state = UiStateModel.from(throwable)

        // When
        presenter.subscribe(state)

        // Then
        verify(view, never()).isLoading()
        verify(view, never()).isSuccess(anyString())
        verify(view, never()).isEmpty()
        verify(view, times(1)).isError(anyString())
    }
}