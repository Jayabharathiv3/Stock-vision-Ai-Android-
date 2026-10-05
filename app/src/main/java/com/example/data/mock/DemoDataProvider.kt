package com.example.data.mock

import com.example.model.*
import kotlin.math.sin
import kotlin.math.cos

object DemoDataProvider {

  val indices = listOf(
    MarketIndex(
      id = "nifty50",
      name = "NIFTY 50",
      symbol = "^NSEI",
      exchange = "NSE",
      value = 24852.15,
      change = 186.40,
      changePercent = 0.76,
      currency = "₹",
      sparkline = listOf(24665.0, 24690.0, 24710.0, 24680.0, 24750.0, 24790.0, 24820.0, 24852.15)
    ),
    MarketIndex(
      id = "sensex",
      name = "SENSEX",
      symbol = "^BSESN",
      exchange = "BSE",
      value = 81432.80,
      change = 542.10,
      changePercent = 0.67,
      currency = "₹",
      sparkline = listOf(80890.0, 80950.0, 81050.0, 81010.0, 81200.0, 81340.0, 81380.0, 81432.80)
    ),
    MarketIndex(
      id = "nasdaq",
      name = "NASDAQ",
      symbol = "^IXIC",
      exchange = "NASDAQ",
      value = 18148.33,
      change = 214.85,
      changePercent = 1.20,
      currency = "$",
      sparkline = listOf(17933.0, 17980.0, 18010.0, 18040.0, 18090.0, 18110.0, 18130.0, 18148.33)
    ),
    MarketIndex(
      id = "sp500",
      name = "S&P 500",
      symbol = "^GSPC",
      exchange = "NYSE",
      value = 5635.80,
      change = -12.40,
      changePercent = -0.22,
      currency = "$",
      sparkline = listOf(5648.0, 5650.0, 5642.0, 5638.0, 5630.0, 5632.0, 5628.0, 5635.80)
    )
  )

  val marketBreadth = MarketBreadth(
    advancing = 1428,
    declining = 894,
    unchanged = 112,
    high52Week = 148,
    low52Week = 24,
    sentimentScore = 72,
    sentimentLabel = "Greed",
    marketStatus = "Market Open"
  )

  val defaultStocks = listOf(
    Stock(
      symbol = "TCS",
      name = "Tata Consultancy Services Ltd.",
      exchange = "NSE",
      currency = "₹",
      currentPrice = 3845.50,
      change = 86.20,
      changePercent = 2.29,
      open = 3770.00,
      high = 3862.00,
      low = 3765.10,
      previousClose = 3759.30,
      volume = 2840500,
      marketCap = "₹13.91T",
      peRatio = 29.8,
      eps = 129.1,
      dividendYield = 1.38,
      high52w = 4254.75,
      low52w = 3313.00,
      rsi = 64.2,
      macd = 18.4,
      sector = "Information Technology",
      description = "Tata Consultancy Services is an Indian multinational information technology services and consulting company, global leader in enterprise digital transformation and AI integration.",
      sparkline = listOf(3760.0, 3775.0, 3790.0, 3782.0, 3810.0, 3830.0, 3845.50),
      isWatchlist = true
    ),
    Stock(
      symbol = "RELIANCE",
      name = "Reliance Industries Ltd.",
      exchange = "NSE",
      currency = "₹",
      currentPrice = 2980.20,
      change = 38.60,
      changePercent = 1.31,
      open = 2948.00,
      high = 2995.00,
      low = 2940.00,
      previousClose = 2941.60,
      volume = 5420100,
      marketCap = "₹20.18T",
      peRatio = 28.4,
      eps = 104.9,
      dividendYield = 0.35,
      high52w = 3217.90,
      low52w = 2221.00,
      rsi = 59.8,
      macd = 12.1,
      sector = "Energy & Conglomerate",
      description = "Reliance Industries is India's largest private sector enterprise, spanning petrochemicals, refining, oil & gas exploration, telecom (Jio), and digital retail.",
      sparkline = listOf(2941.0, 2950.0, 2962.0, 2955.0, 2970.0, 2975.0, 2980.20),
      isWatchlist = true
    ),
    Stock(
      symbol = "INFY",
      name = "Infosys Ltd.",
      exchange = "NSE",
      currency = "₹",
      currentPrice = 1632.75,
      change = -14.25,
      changePercent = -0.87,
      open = 1648.00,
      high = 1655.00,
      low = 1628.00,
      previousClose = 1647.00,
      volume = 4120300,
      marketCap = "₹6.78T",
      peRatio = 25.6,
      eps = 63.8,
      dividendYield = 2.12,
      high52w = 1953.90,
      low52w = 1358.35,
      rsi = 46.5,
      macd = -4.2,
      sector = "Information Technology",
      description = "Infosys is a global leader in next-generation digital services and consulting, enabling clients across 56 countries to navigate their digital transformation powered by Topaz AI.",
      sparkline = listOf(1647.0, 1652.0, 1645.0, 1638.0, 1630.0, 1635.0, 1632.75),
      isWatchlist = true
    ),
    Stock(
      symbol = "HDFCBANK",
      name = "HDFC Bank Ltd.",
      exchange = "NSE",
      currency = "₹",
      currentPrice = 1548.90,
      change = 16.40,
      changePercent = 1.07,
      open = 1535.00,
      high = 1554.00,
      low = 1530.00,
      previousClose = 1532.50,
      volume = 9812400,
      marketCap = "₹11.75T",
      peRatio = 18.2,
      eps = 85.1,
      dividendYield = 1.26,
      high52w = 1794.00,
      low52w = 1363.45,
      rsi = 55.4,
      macd = 6.8,
      sector = "Financial Services",
      description = "HDFC Bank is India's leading private sector bank offering a wide range of banking services across retail, wholesale, and digital banking platforms.",
      sparkline = listOf(1532.0, 1536.0, 1540.0, 1538.0, 1544.0, 1546.0, 1548.90),
      isWatchlist = false
    ),
    Stock(
      symbol = "ICICIBANK",
      name = "ICICI Bank Ltd.",
      exchange = "NSE",
      currency = "₹",
      currentPrice = 1138.40,
      change = 11.20,
      changePercent = 0.99,
      open = 1130.00,
      high = 1142.00,
      low = 1127.50,
      previousClose = 1127.20,
      volume = 7654000,
      marketCap = "₹8.02T",
      peRatio = 17.5,
      eps = 65.0,
      dividendYield = 0.88,
      high52w = 1257.80,
      low52w = 899.00,
      rsi = 58.1,
      macd = 8.5,
      sector = "Financial Services",
      description = "ICICI Bank is a prominent Indian multinational bank and financial services company offering comprehensive financial solutions.",
      sparkline = listOf(1127.0, 1130.0, 1134.0, 1132.0, 1136.0, 1135.0, 1138.40),
      isWatchlist = false
    ),
    Stock(
      symbol = "NVDA",
      name = "NVIDIA Corporation",
      exchange = "NASDAQ",
      currency = "$",
      currentPrice = 129.50,
      change = 4.80,
      changePercent = 3.85,
      open = 125.10,
      high = 130.20,
      low = 124.80,
      previousClose = 124.70,
      volume = 48920000,
      marketCap = "$3.18T",
      peRatio = 42.1,
      eps = 3.08,
      dividendYield = 0.03,
      high52w = 140.76,
      low52w = 45.00,
      rsi = 71.4,
      macd = 3.6,
      sector = "Semiconductors & AI",
      description = "NVIDIA pioneered GPU-accelerated computing to solve some of the world's most complex computational problems, powering the modern generative AI revolution.",
      sparkline = listOf(124.7, 125.5, 126.8, 127.2, 128.4, 129.0, 129.50),
      isWatchlist = true
    ),
    Stock(
      symbol = "AAPL",
      name = "Apple Inc.",
      exchange = "NASDAQ",
      currency = "$",
      currentPrice = 226.80,
      change = 2.45,
      changePercent = 1.09,
      open = 224.50,
      high = 227.40,
      low = 223.90,
      previousClose = 224.35,
      volume = 36800000,
      marketCap = "$3.46T",
      peRatio = 34.2,
      eps = 6.63,
      dividendYield = 0.44,
      high52w = 237.23,
      low52w = 164.08,
      rsi = 62.3,
      macd = 2.1,
      sector = "Consumer Electronics & AI",
      description = "Apple designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories, along with Apple Intelligence ecosystem services.",
      sparkline = listOf(224.35, 225.1, 225.8, 225.4, 226.2, 226.5, 226.80),
      isWatchlist = true
    ),
    Stock(
      symbol = "MSFT",
      name = "Microsoft Corporation",
      exchange = "NASDAQ",
      currency = "$",
      currentPrice = 451.20,
      change = 6.30,
      changePercent = 1.42,
      open = 445.80,
      high = 453.10,
      low = 444.90,
      previousClose = 444.90,
      volume = 19450000,
      marketCap = "$3.35T",
      peRatio = 36.8,
      eps = 12.26,
      dividendYield = 0.67,
      high52w = 468.35,
      low52w = 309.45,
      rsi = 60.5,
      macd = 3.9,
      sector = "Enterprise Cloud & AI",
      description = "Microsoft develops and supports software, services, devices, and enterprise solutions including Azure cloud, Microsoft 365 Copilot, and intelligent platforms.",
      sparkline = listOf(444.9, 446.5, 448.2, 447.8, 449.6, 450.4, 451.20),
      isWatchlist = true
    ),
    Stock(
      symbol = "TSLA",
      name = "Tesla, Inc.",
      exchange = "NASDAQ",
      currency = "$",
      currentPrice = 252.40,
      change = -3.80,
      changePercent = -1.48,
      open = 256.00,
      high = 258.50,
      low = 250.10,
      previousClose = 256.20,
      volume = 52300000,
      marketCap = "$802.4B",
      peRatio = 64.5,
      eps = 3.91,
      dividendYield = 0.0,
      high52w = 271.00,
      low52w = 138.80,
      rsi = 52.8,
      macd = -1.2,
      sector = "Automotive & Clean Energy",
      description = "Tesla designs, manufactures, sells and leases high-performance fully electric vehicles, solar roof systems, and develops Full Self-Driving AI.",
      sparkline = listOf(256.2, 257.0, 255.4, 253.8, 251.5, 252.0, 252.40),
      isWatchlist = false
    ),
    Stock(
      symbol = "GOOGL",
      name = "Alphabet Inc.",
      exchange = "NASDAQ",
      currency = "$",
      currentPrice = 181.60,
      change = 2.10,
      changePercent = 1.17,
      open = 179.80,
      high = 182.40,
      low = 179.20,
      previousClose = 179.50,
      volume = 21200000,
      marketCap = "$2.25T",
      peRatio = 24.3,
      eps = 7.47,
      dividendYield = 0.44,
      high52w = 191.75,
      low52w = 120.21,
      rsi = 58.7,
      macd = 1.8,
      sector = "Internet & AI Search",
      description = "Alphabet is the parent holding company of Google, YouTube, Android, Cloud, Waymo, and DeepMind, pioneering frontier Gemini artificial intelligence.",
      sparkline = listOf(179.5, 180.1, 180.9, 180.5, 181.2, 181.4, 181.60),
      isWatchlist = false
    )
  )

  fun getPredictionsFor(symbol: String, horizon: PredictionHorizon = PredictionHorizon.NEXT_DAY): StockPrediction {
    val stock = defaultStocks.find { it.symbol.equals(symbol, ignoreCase = true) }
      ?: defaultStocks.first()

    val multiplier = when (horizon) {
      PredictionHorizon.NEXT_DAY -> 0.015
      PredictionHorizon.ONE_WEEK -> 0.048
      PredictionHorizon.ONE_MONTH -> 0.112
    }

    val isBullish = stock.rsi > 50 || stock.changePercent > 0
    val changePerc = if (isBullish) {
      stock.changePercent.coerceAtLeast(0.5) + (multiplier * 100)
    } else {
      stock.changePercent.coerceAtMost(-0.5) - (multiplier * 50)
    }

    val targetPrice = stock.currentPrice * (1 + changePerc / 100.0)
    val confidence = when {
      stock.rsi in 40.0..70.0 -> 84 + (stock.symbol.hashCode() % 8)
      else -> 75 + (stock.symbol.hashCode() % 9)
    }.coerceIn(72, 94)

    val signal = when {
      changePerc > 2.0 -> MarketSignal.BULLISH
      changePerc < -2.0 -> MarketSignal.BEARISH
      else -> MarketSignal.NEUTRAL
    }

    val riskLevel = when {
      stock.peRatio > 45 -> RiskLevel.HIGH
      stock.peRatio in 20.0..45.0 -> RiskLevel.MODERATE
      else -> RiskLevel.LOW
    }

    val recommendation = when (signal) {
      MarketSignal.BULLISH -> if (confidence > 85) "Strong Buy" else "Accumulate"
      MarketSignal.BEARISH -> "Reduce / Hedge"
      MarketSignal.NEUTRAL -> "Hold & Observe"
    }

    val drivers = listOf(
      "Positive institutional momentum in ${stock.sector}",
      "RSI indicator standing at ${"%.1f".format(stock.rsi)} signaling ${if (stock.rsi > 50) "healthy expansion" else "consolidation"}",
      "Exponential Moving Average (EMA 50) trading above baseline support",
      "Volume weighted average price (VWAP) holding firmly during market hours",
      "Sentiment index confirms strong buy-side liquidity with low volatility"
    )

    return StockPrediction(
      symbol = stock.symbol,
      currentPrice = stock.currentPrice,
      predictedPrice = targetPrice,
      expectedChangePercent = changePerc,
      confidence = confidence,
      signal = signal,
      horizon = horizon,
      riskLevel = riskLevel,
      recommendation = recommendation,
      priceTrend = if (isBullish) "Upward Bullish Channel" else "Consolidation / Pullback",
      marketSentiment = if (isBullish) "78% Bullish Greed" else "45% Neutral/Cautious",
      technicalAnalysis = "StockVision AI neural models synthesized 48 technical indicators across multitimeframe horizons, projecting favorable risk-adjusted alpha for ${stock.symbol}.",
      keyDrivers = drivers
    )
  }

  fun getHistoricalChartData(symbol: String, period: String): List<ChartDataPoint> {
    val stock = defaultStocks.find { it.symbol.equals(symbol, ignoreCase = true) }
      ?: defaultStocks.first()

    val count = when (period) {
      "1D" -> 24
      "1W" -> 28
      "1M" -> 30
      "3M" -> 45
      "6M" -> 50
      "1Y" -> 52
      "5Y" -> 60
      else -> 30
    }

    val basePrice = stock.currentPrice
    val result = mutableListOf<ChartDataPoint>()
    var currentClose = basePrice * 0.92

    for (i in 0 until count) {
      val trendFactor = (i.toDouble() / count) * 0.12
      val wave = sin(i.toDouble() * 0.4) * 0.02 + cos(i.toDouble() * 0.25) * 0.015
      val open = currentClose
      val change = open * (wave + trendFactor * 0.15)
      val close = (open + change).coerceAtLeast(basePrice * 0.6)
      val high = maxOf(open, close) * (1.0 + (0.008 + (i % 3) * 0.004))
      val low = minOf(open, close) * (1.0 - (0.007 + (i % 2) * 0.003))
      val vol = (1200000 + (sin(i.toDouble()) * 400000).toLong()).coerceAtLeast(500000)

      val label = when (period) {
        "1D" -> "${9 + (i * 15 / 60)}:${(i * 15 % 60).toString().padStart(2, '0')}"
        "1W" -> "Day ${i % 7 + 1}"
        "1M" -> "Day ${i + 1}"
        "3M" -> "Wk ${i / 3 + 1}"
        "6M" -> "Wk ${i / 2 + 1}"
        "1Y" -> "Mo ${i / 4 + 1}"
        "5Y" -> "Yr ${i / 12 + 1}"
        else -> "Pt $i"
      }

      result.add(
        ChartDataPoint(
          timestamp = System.currentTimeMillis() - (count - i) * 86400000L,
          label = label,
          open = open,
          high = high,
          low = low,
          close = close,
          volume = vol
        )
      )
      currentClose = close
    }

    return result
  }

  val demoNews = listOf(
    MarketNews(
      id = "news-1",
      headline = "TCS Bags $1.2B Mega Digital Transformation & AI Deal with European Retail Giant",
      source = "Financial Express",
      date = "2 hours ago",
      relatedStock = "TCS",
      sentiment = NewsSentiment.POSITIVE,
      summary = "Tata Consultancy Services announced a multi-year cloud and enterprise AI contract, bolstering long-term order books and margins."
    ),
    MarketNews(
      id = "news-2",
      headline = "NVIDIA Unveils Next-Gen AI Silicon Architecture to Accelerate Autonomous Computing",
      source = "Bloomberg Tech",
      date = "3 hours ago",
      relatedStock = "NVDA",
      sentiment = NewsSentiment.POSITIVE,
      summary = "NVIDIA's CEO showcased unprecedented compute throughput for LLM inference, driving chip pre-orders from major hyperscalers."
    ),
    MarketNews(
      id = "news-3",
      headline = "Reliance Jio & Retail Expansions Target 22% EBITDA Growth in Upcoming Quarters",
      source = "Economic Times",
      date = "5 hours ago",
      relatedStock = "RELIANCE",
      sentiment = NewsSentiment.POSITIVE,
      summary = "Reliance Industries' diversified telecom and omnichannel retail sectors maintain high consumer retention and cash generation."
    ),
    MarketNews(
      id = "news-4",
      headline = "Central Banks Signal Measured Interest Rate Trajectory Amid Global Inflation Cooling",
      source = "Reuters",
      date = "6 hours ago",
      relatedStock = "HDFCBANK",
      sentiment = NewsSentiment.NEUTRAL,
      summary = "Banking sector analysts anticipate stable net interest margins (NIMs) following calibrated policy cues from monetary authorities."
    ),
    MarketNews(
      id = "news-5",
      headline = "Infosys Reports Steady North America Demand but Flags Cautious BFSI Tech Budgets",
      source = "CNBC-TV18",
      date = "8 hours ago",
      relatedStock = "INFY",
      sentiment = NewsSentiment.NEGATIVE,
      summary = "While AI-led Topaz suites gained enterprise adoption, discretionary spending delays across selective banking clients remain a short-term headwind."
    ),
    MarketNews(
      id = "news-6",
      headline = "Apple Intelligence Ecosystem Features Roll Out Globally Ahead of Holiday Cycle",
      source = "Wall Street Journal",
      date = "10 hours ago",
      relatedStock = "AAPL",
      sentiment = NewsSentiment.POSITIVE,
      summary = "Apple's on-device privacy-first AI enhancements drive accelerated upgrade cycles across premium device lineups."
    )
  )

  val initialHoldings = listOf(
    PortfolioHolding(
      id = 1L,
      symbol = "TCS",
      name = "Tata Consultancy Services Ltd.",
      quantity = 25,
      purchasePrice = 3620.00,
      currentPrice = 3845.50,
      currency = "₹",
      purchaseDate = "12 Aug 2024",
      sector = "Information Technology"
    ),
    PortfolioHolding(
      id = 2L,
      symbol = "RELIANCE",
      name = "Reliance Industries Ltd.",
      quantity = 40,
      purchasePrice = 2810.00,
      currentPrice = 2980.20,
      currency = "₹",
      purchaseDate = "24 Jul 2024",
      sector = "Energy & Conglomerate"
    ),
    PortfolioHolding(
      id = 3L,
      symbol = "NVDA",
      name = "NVIDIA Corporation",
      quantity = 15,
      purchasePrice = 112.40,
      currentPrice = 129.50,
      currency = "$",
      purchaseDate = "05 Sep 2024",
      sector = "Semiconductors & AI"
    )
  )
}
