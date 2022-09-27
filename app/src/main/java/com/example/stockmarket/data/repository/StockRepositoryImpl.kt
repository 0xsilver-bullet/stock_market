package com.example.stockmarket.data.repository

import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.local.StockDao
import com.example.stockmarket.data.mappers.toCompanyListing
import com.example.stockmarket.data.mappers.toCompanyListingEntity
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val stockDao: StockDao,
    private val companyListingsCSVParser: CSVParser<CompanyListing>
) : StockRepository {

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading())
            val localListings = stockDao.searchCompanyListing(query)
            emit(
                Resource.Success(
                    localListings.map { companyListingEntity ->
                        companyListingEntity.toCompanyListing()
                    }
                )
            )
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldLoadFromCacheOnly = !isDbEmpty && !fetchFromRemote
            if (shouldLoadFromCacheOnly) {
                return@flow
            }
            val remoteListings = try {
                val response = api.fetchListings()
                companyListingsCSVParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Unexpected Error"))
                null
            }
            remoteListings?.let { listings ->
                stockDao.clear()
                stockDao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(
                    Resource.Success(
                        data = stockDao.searchCompanyListing("").map { it.toCompanyListing() }
                    )
                )
            }
        }
    }
}