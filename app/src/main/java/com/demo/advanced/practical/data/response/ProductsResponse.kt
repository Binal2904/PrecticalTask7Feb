package com.demo.advanced.practical.data.response

data class ProductsResponse(
    val limit: Int?,
    val products: ArrayList<Product> = arrayListOf(),
    val skip: Int?,
    val total: Int?
)