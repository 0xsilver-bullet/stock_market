package com.example.stockmarket.di

import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.csv.CompanyListingsCSVParser
import com.example.stockmarket.domain.model.CompanyListing
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CSVParserModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsCSVParser(companyListingsCSVParser: CompanyListingsCSVParser): CSVParser<CompanyListing>
}