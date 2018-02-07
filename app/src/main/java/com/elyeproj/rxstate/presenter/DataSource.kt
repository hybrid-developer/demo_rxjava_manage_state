package com.elyeproj.rxstate.presenter

import com.jakewharton.rxrelay2.BehaviorRelay


class DataSource {

    enum class FetchStyle {
        FETCH_SUCCESS,
        FETCH_EMPTY,
        FETCH_ERROR
    }

    var relay: BehaviorRelay<FetchStyle> = BehaviorRelay.createDefault(FetchStyle.FETCH_SUCCESS)
}