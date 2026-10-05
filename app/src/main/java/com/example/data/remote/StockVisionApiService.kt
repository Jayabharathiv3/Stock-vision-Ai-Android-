package com.example.data.remote

import com.example.model.*
import retrofit2.Response
import retrofit2.http.*

interface StockVisionApiService {
  @GET("api/stocks")
  suspend fun getStocks(): Response<List<Stock>>

  @GET("api/stocks/{symbol}")
  suspend fun getStockDetail(@Path("symbol") symbol: String): Response<Stock>

  @GET("api/stocks/{symbol}/history")
  suspend fun getStockHistory(
    @Path("symbol") symbol: String,
    @Query("period") period: String
  ): Response<List<ChartDataPoint>>

  @GET("api/predictions/{symbol}")
  suspend fun getPrediction(
    @Path("symbol") symbol: String,
    @Query("horizon") horizon: String
  ): Response<StockPrediction>

  @GET("api/market/overview")
  suspend fun getMarketOverview(): Response<List<MarketIndex>>

  @GET("api/news")
  suspend fun getMarketNews(): Response<List<MarketNews>>

  @POST("api/auth/login")
  suspend fun login(@Body body: Map<String, String>): Response<Map<String, Any>>

  @POST("api/auth/register")
  suspend fun register(@Body body: Map<String, String>): Response<Map<String, Any>>
}
