package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [PortfolioEntity::class, WatchlistEntity::class],
  version = 1,
  exportSchema = false
)
abstract class StockVisionDatabase : RoomDatabase() {
  abstract fun portfolioDao(): PortfolioDao
  abstract fun watchlistDao(): WatchlistDao

  companion object {
    @Volatile
    private var INSTANCE: StockVisionDatabase? = null

    fun getDatabase(context: Context): StockVisionDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          StockVisionDatabase::class.java,
          "stockvision_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
