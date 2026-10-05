package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "portfolio_holdings")
data class PortfolioEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val symbol: String,
  val name: String,
  val quantity: Int,
  val purchasePrice: Double,
  val purchaseDate: String,
  val currency: String,
  val sector: String
)

@Entity(tableName = "watchlist_items")
data class WatchlistEntity(
  @PrimaryKey val symbol: String,
  val addedAt: Long = System.currentTimeMillis()
)

@Dao
interface PortfolioDao {
  @Query("SELECT * FROM portfolio_holdings ORDER BY id DESC")
  fun getAllHoldings(): Flow<List<PortfolioEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHolding(holding: PortfolioEntity): Long

  @Update
  suspend fun updateHolding(holding: PortfolioEntity)

  @Delete
  suspend fun deleteHolding(holding: PortfolioEntity)

  @Query("DELETE FROM portfolio_holdings WHERE id = :id")
  suspend fun deleteById(id: Long)
}

@Dao
interface WatchlistDao {
  @Query("SELECT * FROM watchlist_items")
  fun getWatchlist(): Flow<List<WatchlistEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToWatchlist(item: WatchlistEntity)

  @Query("DELETE FROM watchlist_items WHERE symbol = :symbol")
  suspend fun removeFromWatchlist(symbol: String)

  @Query("SELECT EXISTS(SELECT 1 FROM watchlist_items WHERE symbol = :symbol)")
  suspend fun isInWatchlist(symbol: String): Boolean
}
