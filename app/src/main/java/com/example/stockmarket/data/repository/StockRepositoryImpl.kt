package com.example.stockmarket.data.repository

import com.example.stockmarket.data.local.StockDao
import com.example.stockmarket.data.mappers.toCompanyListing
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
    private val stockDao: StockDao
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
            val remoteList = try {
                val response = api.fetchListings()
                // TODO: Parse the csv data.
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Unexpected Error"))
            }
        }
    }
}