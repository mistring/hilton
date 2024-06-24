package com.mstringham.ipgeolocation.data.db

/**
 * Repository that provides insert, update, delete, and retrieve of [Geolocation] from a given data source.
 */
interface GeolocationRepo {
    /**
     * Retrieve all the items from the the given data source.
     */
    suspend fun getAllItems(): List<Geolocation>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    suspend fun getItemByQuery(query: String): Geolocation?

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Geolocation): Long

    /**
     * Delete all items from the data source
     */
    suspend fun deleteAllItems()

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Geolocation)
}