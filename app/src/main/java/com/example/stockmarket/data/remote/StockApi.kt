package com.example.stockmarket.data.remote

import com.example.stockmarket.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun fetchListings(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    companion object {
        const val API_KEY = BuildConfig.ALPHA_VANTAGE_API_KEY
        const val BASE_URL = "https://alphavantage.co/"
    }
}