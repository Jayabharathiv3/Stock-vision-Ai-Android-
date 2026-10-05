package com.example.model

enum class MarketSignal {
  BULLISH,
  BEARISH,
  NEUTRAL
}

enum class PredictionHorizon(val label: String, val days: Int) {
  NEXT_DAY("Next Day", 1),
  ONE_WEEK("7 Days", 7),
  ONE_MONTH("30 Days", 30)
}

enum class RiskLevel {
  LOW,
  MODERATE,
  HIGH
}

enum class NewsSentiment {
  POSITIVE,
  NEUTRAL,
  NEGATIVE
}

data class Stock(
  val symbol: String,
  val name: String,
  val exchange: String, // "NSE", "BSE", "NASDAQ", "NYSE"
  val currency: String, // "₹" or "$"
  val currentPrice: Double,
  val change: Double,
  val changePercent: Double,
  val open: Double,
  val high: Double,
  val low: Double,
  val previousClose: Double,
  val volume: Long,
  val marketCap: String,
  val peRatio: Double,
  val eps: Double,
  val dividendYield: Double,
  val high52w: Double,
  val low52w: Double,
  val rsi: Double,
  val macd: Double,
  val sector: String,
  val description: String,
  val sparkline: List<Double> = emptyList(),
  val isWatchlist: Boolean = false
) {
  val isPositive: Boolean get() = change >= 0
  val formattedPrice: String get() = "$currency%,.2f".format(currentPrice)
  val formattedChange: String get() = "%s$currency%,.2f (%s%.2f%%)".format(
    if (change >= 0) "+" else "",
    change,
    if (changePercent >= 0) "+" else "",
    changePercent
  )
}

data class MarketIndex(
  val id: String,
  val name: String,
  val symbol: String,
  val exchange: String,
  val value: Double,
  val change: Double,
  val changePercent: Double,
  val currency: String,
  val sparkline: List<Double>
) {
  val isPositive: Boolean get() = change >= 0
  val formattedValue: String get() = "$currency%,.2f".format(value)
  val formattedChange: String get() = "%s$currency%,.2f (%s%.2f%%)".format(
    if (change >= 0) "+" else "",
    change,
    if (changePercent >= 0) "+" else "",
    changePercent
  )
}

data class ChartDataPoint(
  val timestamp: Long,
  val label: String,
  val open: Double,
  val high: Double,
  val low: Double,
  val close: Double,
  val volume: Long
)

data class StockPrediction(
  val symbol: String,
  val currentPrice: Double,
  val predictedPrice: Double,
  val expectedChangePercent: Double,
  val confidence: Int, // 0 - 100%
  val signal: MarketSignal,
  val horizon: PredictionHorizon,
  val riskLevel: RiskLevel,
  val recommendation: String, // "Strong Buy", "Accumulate", "Hold", "Reduce"
  val priceTrend: String,
  val marketSentiment: String,
  val technicalAnalysis: String,
  val keyDrivers: List<String>,
  val disclaimer: String = "AI predictions are for educational and informational purposes only and should not be considered financial advice."
)

data class PortfolioHolding(
  val id: Long = 0,
  val symbol: String,
  val name: String,
  val quantity: Int,
  val purchasePrice: Double,
  val currentPrice: Double,
  val currency: String = "₹",
  val purchaseDate: String,
  val sector: String = "Technology"
) {
  val investedAmount: Double get() = quantity * purchasePrice
  val currentValue: Double get() = quantity * currentPrice
  val totalProfitLoss: Double get() = currentValue - investedAmount
  val profitLossPercent: Double get() = if (investedAmount > 0) (totalProfitLoss / investedAmount) * 100.0 else 0.0
  val isProfitable: Boolean get() = totalProfitLoss >= 0
}

data class MarketNews(
  val id: String,
  val headline: String,
  val source: String,
  val date: String,
  val relatedStock: String,
  val sentiment: NewsSentiment,
  val summary: String
)

data class MarketBreadth(
  val advancing: Int,
  val declining: Int,
  val unchanged: Int,
  val high52Week: Int,
  val low52Week: Int,
  val sentimentScore: Int, // 0-100 (e.g. 68 Greed)
  val sentimentLabel: String, // "Greed" / "Fear" / "Neutral"
  val marketStatus: String // "Market Open" / "Market Closed"
)
