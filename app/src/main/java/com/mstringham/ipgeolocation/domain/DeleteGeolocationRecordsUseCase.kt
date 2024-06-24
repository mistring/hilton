package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.data.db.GeolocationRepo
import com.mstringham.ipgeolocation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DeleteGeolocationRecordsUseCase @Inject constructor(
    private val geolocationRepo: GeolocationRepo
) {

    operator fun invoke() {
        try {
            runBlocking(Dispatchers.IO) {
                Resource.Success(geolocationRepo.deleteAllItems())
            }
        } catch (ex: Exception) {
            Timber.e("Database Deletion error: ${ex.message}")
            Resource.Error(uiText = UiText.DynamicString("Error deleting all records: ${ex.message}"))
        }
    }

}
