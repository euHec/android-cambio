package com.betrybe.currencyview.data.api

import com.betrybe.currencyview.data.models.CurrencyRateResponse
import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val apiKey = "20EyGYInJopKHhX7HzFTsiWv41CbMtdF"

interface ApiService {
    @Headers("apiKey:$apiKey")
    @GET("symbols")
    suspend fun getSymbol(): Response<CurrencySymbolResponse>
    @Headers("apiKey:$apiKey")
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String)
    : Response<CurrencyRateResponse>
}