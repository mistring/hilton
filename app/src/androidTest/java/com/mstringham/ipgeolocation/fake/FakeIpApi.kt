package com.mstringham.ipgeolocation.fake

import com.mstringham.ipgeolocation.data.api.IpApi
import com.mstringham.ipgeolocation.data.api.IpApiResponse

class FakeIpApi : IpApi {
    override suspend fun getIpGeolocationDetails(query: String): IpApiResponse {
        return FakeDataSource.ipApiResponse
    }
}
