package com.mstringham.ipgeolocation.data.api

import com.google.gson.annotations.SerializedName
import com.mstringham.ipgeolocation.data.db.Geolocation

data class IpApiResponse(
    val query: String = "",

    // "success" or "fail"
    val status: QueryStatus = QueryStatus.fail,

    val country: String = "",

    val countryCode: String = "",

    val region: String = "",

    val regionName: String = "",

    val city: String = "",

    val zip: String = "",

    @SerializedName("lat")
    val latitude: Double = 0.0,

    @SerializedName("lon")
    val longitude: Double = 0.0,

    val timezone: String = "",

    @SerializedName("isp")
    val internetServiceProvider: String = "",

    @SerializedName("org")
    val organization: String = "",

    @SerializedName("as")
    val autonomousSystem: String = ""
) {
    fun toDbEntity(): Geolocation {
        return Geolocation(
            query = query,
            status = status.name,
            country = country,
            countryCode = countryCode,
            region = region,
            regionName = regionName,
            city = city,
            zip = zip,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            internetServiceProvider = internetServiceProvider,
            organization = organization,
            autonomousSystem = autonomousSystem
        )

    }

}

enum class QueryStatus {
    success, fail
}
