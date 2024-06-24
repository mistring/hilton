package com.mstringham.ipgeolocation


import com.mstringham.ipgeolocation.data.api.NetworkIpApiRepo
import com.mstringham.ipgeolocation.fake.FakeDataSource
import com.mstringham.ipgeolocation.fake.FakeIpApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkIpApiTest {

    @Test
    fun networkIpApiRepository_getIpGeolocationDetails_verifyIpApiResponse() =
        runTest {
            val repository = NetworkIpApiRepo(
                ipApi = FakeIpApi()
            )
            assertEquals(FakeDataSource.ipApiResponse, repository.getIpGeolocationDetails(""))
        }
}