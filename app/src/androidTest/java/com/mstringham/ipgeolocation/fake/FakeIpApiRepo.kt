package com.mstringham.ipgeolocation.fake

import com.mstringham.ipgeolocation.data.api.IpApiRepo
import com.mstringham.ipgeolocation.data.api.IpApiResponse

class FakeIpApiRepo : IpApiRepo {
    override suspend fun getIpGeolocationDetails(query: String): IpApiResponse {
        return FakeDataSource.ipApiResponse
    }
}