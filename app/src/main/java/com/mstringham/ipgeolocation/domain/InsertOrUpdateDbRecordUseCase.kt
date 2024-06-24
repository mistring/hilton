package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.data.db.GeolocationRepo
import com.mstringham.ipgeolocation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class InsertOrUpdateDbRecordUseCase @Inject constructor(
    private val itemsRepository: GeolocationRepo
) {

    operator fun invoke(geolocation: Geolocation): Resource<Long> {
        return try {
            runBlocking(Dispatchers.IO) {
                Resource.Success(itemsRepository.insertItem(geolocation))
            }
        } catch (ex: Exception) {
            Timber.e("Database Insertion error: ${ex.message}")
            Resource.Error(uiText = UiText.DynamicString("Database Insert Item Error: ${ex.message}"))
        }
    }

}