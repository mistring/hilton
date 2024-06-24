package com.mstringham.ipgeolocation.domain

import com.mstringham.ipgeolocation.data.api.IpApiRepo
import com.mstringham.ipgeolocation.data.api.IpApiResponse
import com.mstringham.ipgeolocation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class GetIpApiResponseUseCase @Inject constructor(
    private val ipApiRepo: IpApiRepo
) {

    operator fun invoke(query: String): Resource<IpApiResponse> {
        return try {
            runBlocking(Dispatchers.IO) {
                Resource.Success(ipApiRepo.getIpGeolocationDetails(query))
            }
        } catch (ex: Exception) {
            Timber.e("Networking API error: ${ex.message}")
            Resource.Error(uiText = UiText.DynamicString("Network API Error: ${ex.message}"))
        }
    }

}
