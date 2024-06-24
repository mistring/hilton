package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.data.db.GeolocationRepo
import com.mstringham.ipgeolocation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class GetGeolocationRecordsUseCase @Inject constructor(
    private val geolocationRepo: GeolocationRepo
) {

    operator fun invoke(): Resource<List<Geolocation>> {
        return try {
            runBlocking(Dispatchers.IO) {
                Resource.Success(geolocationRepo.getAllItems())
            }
        } catch (ex: Exception) {
            Timber.e("Database Search Items error: ${ex.message}")
            Resource.Error(uiText = UiText.DynamicString("Database Fetch Items Error: ${ex.message}"))
        }
    }

}