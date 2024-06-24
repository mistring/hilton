package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.data.db.GeolocationRepo
import com.mstringham.ipgeolocation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class GetGeolocationRecordByQueryUseCase @Inject constructor(
    private val geolocationRepo: GeolocationRepo
) {

    operator fun invoke(query: String): Resource<Geolocation> {
        return try {
            runBlocking(Dispatchers.IO) {
                Resource.Success(geolocationRepo.getItemByQuery(query))
            }
        } catch (ex: Exception) {
            Timber.e("Database Search Item error: ${ex.message}")
            Resource.Error(uiText = UiText.DynamicString("Error searching database: ${ex.message}"))
        }
    }

}