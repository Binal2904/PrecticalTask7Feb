package com.demo.advanced.practical.util

import com.demo.advanced.practical.enum.ApiStatus

data class ApiResource<out T>(val status: ApiStatus, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): ApiResource<T> {
            return ApiResource(ApiStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ApiResource<T> {
            return ApiResource(ApiStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ApiResource<T> {
            return ApiResource(ApiStatus.LOADING, data, null)
        }

    }

}