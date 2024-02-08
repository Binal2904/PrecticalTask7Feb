package com.demo.advanced.practical.repository

import com.demo.advanced.practical.restfullapi.ApiService

class MainRepository(private val apiHelper: ApiService) {

    suspend fun getProducts() = apiHelper.getProducts()
}