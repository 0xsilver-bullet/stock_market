package com.example.stockmarket.di

import android.content.Context
import androidx.room.Room
import com.example.stockmarket.data.local.StockDao
import com.example.stockmarket.data.local.StockDatabase
import com.example.stockmarket.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockMarketApi(): StockApi {
        return Retrofit
            .Builder()
            .baseUrl(StockApi.BASE_URL)
            .build()
            .create(StockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room
            .databaseBuilder(
                context,
                StockDatabase::class.java,
                "StockDatabase.db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSockDao(stockDatabase: StockDatabase): StockDao = stockDatabase.stockDao
}