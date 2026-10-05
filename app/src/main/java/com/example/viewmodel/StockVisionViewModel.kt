package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PortfolioEntity
import com.example.data.mock.DemoDataProvider
import com.example.data.repository.StockVisionRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
  LANDING,
  LOGIN,
  REGISTER,
  DASHBOARD,
  STOCK_DETAILS,
  PREDICTIONS,
  COMPARE,
  PORTFOLIO,
  WATCHLIST,
  NEWS,
  SETTINGS,
  NOT_FOUND
}

data class UserSession(
  val name: String,
  val email: String,
  val accountTier: String = "Pro Investor & Student Tier"
)

class StockVisionViewModel(application: Application) : AndroidViewModel(application) {

  val repository = StockVisionRepository(application)

  private val _screenStack = MutableStateFlow(listOf(AppScreen.DASHBOARD))
  val currentScreen: StateFlow<AppScreen> = _screenStack.map { it.lastOrNull() ?: AppScreen.DASHBOARD }
    .stateIn(viewModelScope, SharingStarted.Eagerly, AppScreen.DASHBOARD)

  private val _isDarkTheme = MutableStateFlow(true)
  val isDarkTheme = _isDarkTheme.asStateFlow()

  private val _userSession = MutableStateFlow<UserSession?>(
    UserSession("Aarav Sharma", "aarav.investor@stockvision.ai")
  )
  val userSession = _userSession.asStateFlow()

  private val _indices = MutableStateFlow<List<MarketIndex>>(emptyList())
  val indices = _indices.asStateFlow()

  private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
  val stocks = _stocks.asStateFlow()

  private val _marketBreadth = MutableStateFlow(DemoDataProvider.marketBreadth)
  val marketBreadth = _marketBreadth.asStateFlow()

  private val _selectedStockSymbol = MutableStateFlow("TCS")
  val selectedStockSymbol = _selectedStockSymbol.asStateFlow()

  private val _selectedStock = MutableStateFlow<Stock?>(null)
  val selectedStock = _selectedStock.asStateFlow()

  private val _chartPeriod = MutableStateFlow("1M")
  val chartPeriod = _chartPeriod.asStateFlow()

  private val _isCandleMode = MutableStateFlow(false)
  val isCandleMode = _isCandleMode.asStateFlow()

  private val _chartData = MutableStateFlow<List<ChartDataPoint>>(emptyList())
  val chartData = _chartData.asStateFlow()

  private val _predictionHorizon = MutableStateFlow(PredictionHorizon.NEXT_DAY)
  val predictionHorizon = _predictionHorizon.asStateFlow()

  private val _currentPrediction = MutableStateFlow<StockPrediction?>(null)
  val currentPrediction = _currentPrediction.asStateFlow()

  private val _marketNews = MutableStateFlow<List<MarketNews>>(emptyList())
  val marketNews = _marketNews.asStateFlow()

  private val _portfolioHoldings = MutableStateFlow<List<PortfolioHolding>>(emptyList())
  val portfolioHoldings = _portfolioHoldings.asStateFlow()

  private val _compareSymbols = MutableStateFlow(listOf("TCS", "INFY", "NVDA", "AAPL"))
  val compareSymbols = _compareSymbols.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery = _searchQuery.asStateFlow()

  private val _searchFilter = MutableStateFlow("All")
  val searchFilter = _searchFilter.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading = _isLoading.asStateFlow()

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage = _errorMessage.asStateFlow()

  private val _notifications = MutableStateFlow(
    listOf(
      "AI Alert: TCS target achieved with 87% neural confidence",
      "Market Breadth: Advancing issues hit 1,428 outperforming benchmark",
      "Portfolio Update: NVDA profit exceeded +15.2% target band"
    )
  )
  val notifications = _notifications.asStateFlow()

  val isDemoMode = repository.isDemoMode
  val backendStatus = repository.backendStatus
  val apiUrl = repository.currentApiUrl

  init {
    viewModelScope.launch {
      repository.seedInitialDataIfEmpty()
      loadInitialData()
      observeDatabase()
    }
  }

  fun navigateTo(screen: AppScreen) {
    _screenStack.update { current ->
      if (current.lastOrNull() == screen) current else current + screen
    }
  }

  fun popBack(): Boolean {
    if (_screenStack.value.size > 1) {
      _screenStack.update { it.dropLast(1) }
      return true
    }
    return false
  }

  fun openStockDetails(symbol: String) {
    _selectedStockSymbol.value = symbol
    loadStockDetails(symbol)
    navigateTo(AppScreen.STOCK_DETAILS)
  }

  fun openPredictionsFor(symbol: String) {
    _selectedStockSymbol.value = symbol
    loadPredictions(symbol, _predictionHorizon.value)
    navigateTo(AppScreen.PREDICTIONS)
  }

  fun toggleTheme() {
    _isDarkTheme.value = !_isDarkTheme.value
  }

  fun setChartPeriod(period: String) {
    _chartPeriod.value = period
    loadChartData(_selectedStockSymbol.value, period)
  }

  fun toggleCandleMode() {
    _isCandleMode.value = !_isCandleMode.value
  }

  fun setPredictionHorizon(horizon: PredictionHorizon) {
    _predictionHorizon.value = horizon
    loadPredictions(_selectedStockSymbol.value, horizon)
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSearchFilter(filter: String) {
    _searchFilter.value = filter
  }

  fun toggleWatchlist(symbol: String) {
    viewModelScope.launch {
      val isNowInWatchlist = repository.toggleWatchlist(symbol)
      _stocks.update { current ->
        current.map { if (it.symbol == symbol) it.copy(isWatchlist = isNowInWatchlist) else it }
      }
      if (_selectedStock.value?.symbol == symbol) {
        _selectedStock.update { it?.copy(isWatchlist = isNowInWatchlist) }
      }
    }
  }

  fun toggleCompareSymbol(symbol: String) {
    val current = _compareSymbols.value.toMutableList()
    if (current.contains(symbol)) {
      if (current.size > 1) {
        current.remove(symbol)
        _compareSymbols.value = current
      }
    } else {
      if (current.size < 4) {
        current.add(symbol)
        _compareSymbols.value = current
      }
    }
  }

  fun addPortfolioHolding(
    symbol: String,
    quantity: Int,
    purchasePrice: Double
  ) {
    viewModelScope.launch {
      val stock = _stocks.value.find { it.symbol.equals(symbol, ignoreCase = true) }
      val name = stock?.name ?: "$symbol Corp"
      val currency = stock?.currency ?: "₹"
      val sector = stock?.sector ?: "Equities"

      repository.addHolding(
        PortfolioEntity(
          symbol = symbol.uppercase(),
          name = name,
          quantity = quantity,
          purchasePrice = purchasePrice,
          purchaseDate = "05 Oct 2026",
          currency = currency,
          sector = sector
        )
      )
    }
  }

  fun removePortfolioHolding(id: Long) {
    viewModelScope.launch {
      repository.removeHolding(id)
    }
  }

  fun login(name: String, email: String) {
    _userSession.value = UserSession(name.ifBlank { "Investor" }, email)
    navigateTo(AppScreen.DASHBOARD)
  }

  fun logout() {
    _userSession.value = null
    navigateTo(AppScreen.LOGIN)
  }

  fun updateApiUrl(url: String) {
    repository.updateApiUrl(url)
    refreshAll()
  }

  fun toggleDemoMode(enabled: Boolean) {
    repository.toggleDemoMode(enabled)
    refreshAll()
  }

  fun refreshAll() {
    viewModelScope.launch {
      _isLoading.value = true
      _errorMessage.value = null
      try {
        loadInitialData()
      } catch (e: Exception) {
        _errorMessage.value = "Failed to sync: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  private suspend fun loadInitialData() {
    _isLoading.value = true
    try {
      _indices.value = repository.fetchMarketOverview()
      _stocks.value = repository.fetchStocks()
      _marketNews.value = repository.fetchNews()
      loadStockDetails(_selectedStockSymbol.value)
    } catch (e: Exception) {
      _errorMessage.value = "Network error: Using offline demo intelligence"
    } finally {
      _isLoading.value = false
    }
  }

  private fun loadStockDetails(symbol: String) {
    viewModelScope.launch {
      val detail = repository.fetchStockDetail(symbol)
      _selectedStock.value = detail
      loadChartData(symbol, _chartPeriod.value)
      loadPredictions(symbol, _predictionHorizon.value)
    }
  }

  private fun loadChartData(symbol: String, period: String) {
    viewModelScope.launch {
      _chartData.value = repository.fetchStockHistory(symbol, period)
    }
  }

  private fun loadPredictions(symbol: String, horizon: PredictionHorizon) {
    viewModelScope.launch {
      _currentPrediction.value = repository.fetchPredictions(symbol, horizon)
    }
  }

  private fun observeDatabase() {
    viewModelScope.launch {
      repository.getPortfolioEntities().collect { entities ->
        val currentStocks = _stocks.value
        _portfolioHoldings.value = entities.map { entity ->
          val current = currentStocks.find { it.symbol == entity.symbol }?.currentPrice ?: entity.purchasePrice
          PortfolioHolding(
            id = entity.id,
            symbol = entity.symbol,
            name = entity.name,
            quantity = entity.quantity,
            purchasePrice = entity.purchasePrice,
            currentPrice = current,
            currency = entity.currency,
            purchaseDate = entity.purchaseDate,
            sector = entity.sector
          )
        }
      }
    }
  }
}
