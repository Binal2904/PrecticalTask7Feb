package com.demo.advanced.practical.restfullapi

import com.demo.advanced.practical.data.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>
}