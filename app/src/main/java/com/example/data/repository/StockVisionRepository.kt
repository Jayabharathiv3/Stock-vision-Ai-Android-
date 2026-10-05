package com.example.data.repository

import android.content.Context
import com.example.data.local.PortfolioDao
import com.example.data.local.PortfolioEntity
import com.example.data.local.StockVisionDatabase
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.mock.DemoDataProvider
import com.example.data.remote.StockVisionApiService
import com.example.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class StockVisionRepository(context: Context) {

  private val database = StockVisionDatabase.getDatabase(context)
  private val portfolioDao: PortfolioDao = database.portfolioDao()
  private val watchlistDao: WatchlistDao = database.watchlistDao()

  private val prefs = context.getSharedPreferences("stockvision_prefs", Context.MODE_PRIVATE)

  private val _currentApiUrl = MutableStateFlow(
    prefs.getString("api_url", "https://api.stockvision.ai/") ?: "https://api.stockvision.ai/"
  )
  val currentApiUrl = _currentApiUrl.asStateFlow()

  private val _isDemoMode = MutableStateFlow(true)
  val isDemoMode = _isDemoMode.asStateFlow()

  private val _backendStatus = MutableStateFlow("Using Demo Data (Default)")
  val backendStatus = _backendStatus.asStateFlow()

  private var apiService: StockVisionApiService? = null

  init {
    initApiService(_currentApiUrl.value)
  }

  fun updateApiUrl(newUrl: String) {
    var formatted = newUrl.trim()
    if (!formatted.endsWith("/")) {
      formatted += "/"
    }
    _currentApiUrl.value = formatted
    prefs.edit().putString("api_url", formatted).apply()
    initApiService(formatted)
  }

  fun toggleDemoMode(enabled: Boolean) {
    _isDemoMode.value = enabled
    _backendStatus.value = if (enabled) "Demo Mode Enabled" else "Live Backend Mode"
  }

  private fun initApiService(baseUrl: String) {
    try {
      val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

      val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

      val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

      apiService = retrofit.create(StockVisionApiService::class.java)
    } catch (e: Exception) {
      apiService = null
      _backendStatus.value = "Failed to initialize API client: ${e.message}"
    }
  }

  suspend fun fetchStocks(): List<Stock> = withContext(Dispatchers.IO) {
    val watchlistSymbols = try {
      watchlistDao.getWatchlist().first().map { it.symbol }.toSet()
    } catch (e: Exception) {
      emptySet()
    }

    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getStocks()
        if (response.isSuccessful && response.body() != null) {
          _backendStatus.value = "Connected to Live API"
          return@withContext response.body()!!.map { stock ->
            stock.copy(isWatchlist = watchlistSymbols.contains(stock.symbol))
          }
        }
      } catch (e: Exception) {
        _backendStatus.value = "API unreachable: Using Fallback Demo Data"
      }
    }

    // Return rich mock data
    DemoDataProvider.defaultStocks.map { stock ->
      stock.copy(isWatchlist = watchlistSymbols.contains(stock.symbol) || stock.isWatchlist)
    }
  }

  suspend fun fetchStockDetail(symbol: String): Stock = withContext(Dispatchers.IO) {
    val isWatch = try {
      watchlistDao.isInWatchlist(symbol)
    } catch (e: Exception) {
      false
    }

    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getStockDetail(symbol)
        if (response.isSuccessful && response.body() != null) {
          return@withContext response.body()!!.copy(isWatchlist = isWatch)
        }
      } catch (e: Exception) {
        // Fallback to demo
      }
    }

    val stock = DemoDataProvider.defaultStocks.find { it.symbol.equals(symbol, ignoreCase = true) }
      ?: Stock(
        symbol = symbol.uppercase(),
        name = "$symbol Corporation",
        exchange = "GLOBAL",
        currency = "$",
        currentPrice = 150.0,
        change = 2.5,
        changePercent = 1.69,
        open = 148.0,
        high = 152.0,
        low = 147.5,
        previousClose = 147.5,
        volume = 1200000L,
        marketCap = "$10.5B",
        peRatio = 22.4,
        eps = 6.7,
        dividendYield = 1.2,
        high52w = 175.0,
        low52w = 110.0,
        rsi = 56.4,
        macd = 1.5,
        sector = "Diversified Technology",
        description = "Automated market asset tracking profile for $symbol analyzed by StockVision AI platform."
      )

    stock.copy(isWatchlist = isWatch || stock.isWatchlist)
  }

  suspend fun fetchStockHistory(symbol: String, period: String): List<ChartDataPoint> = withContext(Dispatchers.IO) {
    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getStockHistory(symbol, period)
        if (response.isSuccessful && response.body() != null) {
          return@withContext response.body()!!
        }
      } catch (e: Exception) {
        // Fallback to demo
      }
    }
    DemoDataProvider.getHistoricalChartData(symbol, period)
  }

  suspend fun fetchPredictions(symbol: String, horizon: PredictionHorizon): StockPrediction = withContext(Dispatchers.IO) {
    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getPrediction(symbol, horizon.name)
        if (response.isSuccessful && response.body() != null) {
          return@withContext response.body()!!
        }
      } catch (e: Exception) {
        // Fallback to demo
      }
    }
    DemoDataProvider.getPredictionsFor(symbol, horizon)
  }

  suspend fun fetchMarketOverview(): List<MarketIndex> = withContext(Dispatchers.IO) {
    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getMarketOverview()
        if (response.isSuccessful && response.body() != null) {
          return@withContext response.body()!!
        }
      } catch (e: Exception) {
        // Fallback to demo
      }
    }
    DemoDataProvider.indices
  }

  suspend fun fetchNews(): List<MarketNews> = withContext(Dispatchers.IO) {
    if (!_isDemoMode.value && apiService != null) {
      try {
        val response = apiService!!.getMarketNews()
        if (response.isSuccessful && response.body() != null) {
          return@withContext response.body()!!
        }
      } catch (e: Exception) {
        // Fallback to demo
      }
    }
    DemoDataProvider.demoNews
  }

  // Watchlist Local Operations
  suspend fun toggleWatchlist(symbol: String): Boolean = withContext(Dispatchers.IO) {
    val exists = watchlistDao.isInWatchlist(symbol)
    if (exists) {
      watchlistDao.removeFromWatchlist(symbol)
      false
    } else {
      watchlistDao.addToWatchlist(WatchlistEntity(symbol = symbol))
      true
    }
  }

  fun getWatchlistEntities(): Flow<List<WatchlistEntity>> = watchlistDao.getWatchlist()

  // Portfolio Local Operations
  fun getPortfolioEntities(): Flow<List<PortfolioEntity>> = portfolioDao.getAllHoldings()

  suspend fun addHolding(holding: PortfolioEntity) = withContext(Dispatchers.IO) {
    portfolioDao.insertHolding(holding)
  }

  suspend fun removeHolding(id: Long) = withContext(Dispatchers.IO) {
    portfolioDao.deleteById(id)
  }

  suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
    try {
      val existing = portfolioDao.getAllHoldings().first()
      if (existing.isEmpty()) {
        DemoDataProvider.initialHoldings.forEach { holding ->
          portfolioDao.insertHolding(
            PortfolioEntity(
              symbol = holding.symbol,
              name = holding.name,
              quantity = holding.quantity,
              purchasePrice = holding.purchasePrice,
              purchaseDate = holding.purchaseDate,
              currency = holding.currency,
              sector = holding.sector
            )
          )
        }
      }
      val existingWatch = watchlistDao.getWatchlist().first()
      if (existingWatch.isEmpty()) {
        listOf("TCS", "RELIANCE", "INFY", "NVDA", "AAPL", "MSFT").forEach { symbol ->
          watchlistDao.addToWatchlist(WatchlistEntity(symbol = symbol))
        }
      }
    } catch (e: Exception) {
      // Ignore
    }
  }
}
