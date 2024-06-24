package com.mstringham.ipgeolocation.domain

import javax.inject.Inject

data class AppUseCases @Inject constructor(
    val getDatabaseRecords: GetGeolocationRecordsUseCase,
    val deleteDatabaseRecords: DeleteGeolocationRecordsUseCase,
    val upsertDatabaseRecord: InsertOrUpdateDbRecordUseCase,
    val getDatabaseRecordByQuery: GetGeolocationRecordByQueryUseCase,
    val getNetworkResult: GetIpApiResponseUseCase,
    val isQueryValid: ValidateQueryUseCase
)
