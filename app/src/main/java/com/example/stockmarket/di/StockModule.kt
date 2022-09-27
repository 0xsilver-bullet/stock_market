package com.example.stockmarket.di

import com.example.stockmarket.data.repository.StockRepositoryImpl
import com.example.stockmarket.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StockModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(stockRepository: StockRepositoryImpl): StockRepository
}