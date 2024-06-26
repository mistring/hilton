package com.mstringham.ipgeolocation.ui.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mstringham.ipgeolocation.R
import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.domain.AppUseCases
import com.mstringham.ipgeolocation.domain.Resource
import com.mstringham.ipgeolocation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Ui State for History Screen
 */
sealed interface HistoryState {
    data object Loading : HistoryState
    data class Results(val itemList: List<Geolocation>) : HistoryState
    data class Error(val error: UiText?) : HistoryState
}


/**
 * ViewModel to fetch all items from the [Geolocation] Entity of the Room Database.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val appUseCases: AppUseCases
) : ViewModel() {

    // TODO: This should be a state flow
    var state: HistoryState by mutableStateOf(HistoryState.Loading)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCachedItems()
        }
    }

    private suspend fun fetchCachedItems() {
        Timber.d("Fetch from DB")

        val nextState: HistoryState = when (val cacheResult = appUseCases.getDatabaseRecords()) {
            is Resource.Success -> {
                cacheResult.data?.let { dbResult ->
                    HistoryState.Results(dbResult)
                } ?: run {
                    HistoryState.Error(UiText.StringResource(id = R.string.invalid_db_results))
                }
            }

            is Resource.Error -> {
                Timber.e("Room DB Error")
                HistoryState.Error(cacheResult.errorMessage)
            }
        }
        updateState(nextState)

    }

    private suspend fun updateState(nextState: HistoryState) {
        withContext(Dispatchers.Main) {
            state = nextState
        }
    }

    fun deleteAllHistoryItems() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(HistoryState.Loading)
            appUseCases.deleteDatabaseRecords()
            delay(300L)
            fetchCachedItems()
        }
    }

}
