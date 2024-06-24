package com.mstringham.ipgeolocation.fake

import com.mstringham.ipgeolocation.data.api.IpApiResponse
import com.mstringham.ipgeolocation.data.api.QueryStatus
import com.mstringham.ipgeolocation.data.db.Geolocation

object FakeDataSource {

    val ipApiResponse =
        IpApiResponse(
            query = "199.60.103.11",
            status = QueryStatus.success,
            country = "United States",
            countryCode = "US",
            region = "MA",
            regionName = "Massachusetts",
            city = "Cambridge",
            zip = "02141",
            latitude = 42.3698,
            longitude = -71.0774,
            timezone = "America/New_York",
            internetServiceProvider = "Cloudflare London, LLC",
            organization = "HubSpot, Inc.",
            autonomousSystem = "AS209242 Cloudflare London, LLC"
        )


    val geolocationRecord =
        Geolocation(
            id = 1,
            status = "success",
            country = "United States",
            countryCode = "US",
            region = "MA",
            regionName = "Massachusetts",
            city = "Cambridge",
            zip = "02141",
            latitude = 42.3698,
            longitude = -71.0774,
            timezone = "America/New_York",
            internetServiceProvider = "Cloudflare London, LLC",
            organization = "HubSpot, Inc.",
            autonomousSystem = "AS209242 Cloudflare London, LLC",
            query = "199.60.103.11",
            timestamp = 1716066210610
        )
}