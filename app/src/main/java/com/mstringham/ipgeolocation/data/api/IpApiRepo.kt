package com.mstringham.ipgeolocation.data.api

/**
 * Repository that fetch IP Geolocation details
 */
interface IpApiRepo {
    suspend fun getIpGeolocationDetails(query: String): IpApiResponse
}

/**
 * Network Implementation of Repository
 */
class NetworkIpApiRepo(
    private val ipApi: IpApi
) : IpApiRepo {
    override suspend fun getIpGeolocationDetails(query: String): IpApiResponse = ipApi.getIpGeolocationDetails(query)
}
