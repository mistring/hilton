package com.mstringham.ipgeolocation.data.db

import android.text.format.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mstringham.ipgeolocation.data.api.QueryStatus


/**
 * Entity data class where each instance represents a single row in the database.
 */
@Entity(
    tableName = "geolocation",
    indices = [Index(value = ["query"], unique = true)]
)
data class Geolocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val query: String,
    val status: String,
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionName: String = "",
    val city: String = "",
    val zip: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",

    @ColumnInfo(name = "isp")
    val internetServiceProvider: String = "",

    @ColumnInfo(name = "org")
    val organization: String = "",

    @ColumnInfo(name = "as")
    val autonomousSystem: String = "",

    var timestamp: Long = System.currentTimeMillis()
) {
    fun isSuccess() = status == QueryStatus.success.name

    fun formatTimestamp(): String {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", timestamp).toString()
    }
}
