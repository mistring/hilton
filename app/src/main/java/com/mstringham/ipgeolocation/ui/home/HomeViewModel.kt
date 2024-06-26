package com.mstringham.ipgeolocation.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mstringham.ipgeolocation.R
import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.domain.AppUseCases
import com.mstringham.ipgeolocation.domain.Resource
import com.mstringham.ipgeolocation.domain.Validation
import com.mstringham.ipgeolocation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


/**
 * Ui State for Home Screen
 */
sealed interface HomeState {
    data object Idle : HomeState
    data object Loading : HomeState
    data object Network : HomeState
    data class Result(val currentResult: Geolocation, val source: QuerySource = QuerySource.LOCAL) : HomeState
    data class Error(val error: UiText?) : HomeState
}

enum class QuerySource {
    LOCAL,
    REMOTE
}

/**
 * ViewModel for Home Screen
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases
) : ViewModel() {

    // TODO: This should be a state flow
    var state: HomeState by mutableStateOf(HomeState.Idle)
        private set

    // TODO: This mutableStateOf should be pushed down into the Composable, and then a one-way call to update the source-of-truth here in the ViewModel
    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun issueIpGeolocationQuery() = viewModelScope.launch(Dispatchers.IO) {
        updateState(HomeState.Loading)

        // Ensure that leading and trailing whitespace is removed (example of applying business rules)
        searchQuery = Validation.sanitize(searchQuery)

        val validationResult = appUseCases.isQueryValid(searchQuery)
        if (validationResult.successful) {
            searchDatabaseForQuery()
        } else {
            updateState(HomeState.Error(validationResult.errorMessage))
        }
    }

    private suspend fun searchDatabaseForQuery() {
        Timber.d("Fetch from DB")

        val nextState: HomeState = when (val cacheResult = appUseCases.getDatabaseRecordByQuery(searchQuery)) {
            is Resource.Success -> {
                cacheResult.data?.let { dbResult ->
                    if (dbResult.timestamp < System.currentTimeMillis() - STALE_MILLIS) {
                        callApiWithQuery()
                        HomeState.Network // Update state: network call is running
                    } else {
                        HomeState.Result(currentResult = dbResult, source = QuerySource.LOCAL)
                    }

                } ?: run {
                    callApiWithQuery()
                    HomeState.Network // Update state: network call is running
                }
            }

            is Resource.Error -> {
                Timber.i("Home State: Error")
                HomeState.Error(cacheResult.errorMessage)
            }
        }
        if (nextState != HomeState.Network) {
            updateState(nextState)
        }

    }

    private suspend fun callApiWithQuery() {
        Timber.d("Fetch from API")

        val nextState: HomeState = when (val networkResult = appUseCases.getNetworkResult(searchQuery)) {
            is Resource.Success -> {
                networkResult.data?.let { apiResponse ->
                    val dbEntity = apiResponse.toDbEntity()
                    Timber.d("entity: $dbEntity")
                    if (dbEntity.isSuccess()) {
                        when (val insertResult = appUseCases.upsertDatabaseRecord(dbEntity)) {
                            is Resource.Success -> {
                                HomeState.Result(currentResult = dbEntity, source = QuerySource.REMOTE)
                            }

                            is Resource.Error -> {
                                Timber.i("DB Save Error")
                                HomeState.Error(insertResult.errorMessage)
                            }
                        }
                    } else {
                        HomeState.Error(UiText.StringResource(id = R.string.invalid_api_result))
                    }

                } ?: HomeState.Error(networkResult.errorMessage)
            }

            is Resource.Error -> {
                Timber.d("API Error")
                HomeState.Error(networkResult.errorMessage)
            }
        }
        updateState(nextState)
    }

    private suspend fun updateState(nextState: HomeState) {
        withContext(Dispatchers.Main) {
            state = nextState
        }
    }

    companion object {
        private const val STALE_MILLIS = 5 * 60 * 1_000L // 5 minutes
    }
}
