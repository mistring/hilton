package com.mstringham.ipgeolocation.data.db

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GeolocationRepoImpl @Inject constructor(
    private val itemDao: GeolocationDao
) : GeolocationRepo {
    override suspend fun getAllItems(): List<Geolocation> = itemDao.getAllItems()

    override suspend fun getItemByQuery(query: String): Geolocation? = itemDao.getItemByQuery(query)

    override suspend fun insertItem(item: Geolocation): Long = itemDao.insert(item)

    override suspend fun deleteAllItems() = itemDao.deleteAllItems()

    override suspend fun updateItem(item: Geolocation) = itemDao.update(item)
}