package com.mstringham.ipgeolocation.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface IpApi {

    @GET("json/{query}")
    suspend fun getIpGeolocationDetails(@Path("query") query: String): IpApiResponse

}