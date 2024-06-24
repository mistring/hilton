package com.mstringham.ipgeolocation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.data.db.GeolocationDao
import com.mstringham.ipgeolocation.data.db.GeolocationDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GeolocationDatabaseTest {
    private lateinit var geolocationDao: GeolocationDao
    private lateinit var geolocationDatabase: GeolocationDatabase

    private var item1 = Geolocation(id = 1, query = "84.104.16.12", status = "success", country = "US")
    private var item2 = Geolocation(id = 2, query = "18.81.3.97", status = "success", country = "US")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        geolocationDatabase = Room.inMemoryDatabaseBuilder(context, GeolocationDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        geolocationDao = geolocationDatabase.geolocationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        geolocationDatabase.close()
    }

    private suspend fun addOneItemToDb() {
        geolocationDao.insert(item1)
    }

    private suspend fun addTwoItemsToDb() {
        geolocationDao.insert(item1)
        geolocationDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val firstItem = geolocationDao.getAllItems().first()
        assertEquals(firstItem, item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = geolocationDao.getAllItems()
        assertEquals(allItems[0], item2)
        assertEquals(allItems[1], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemsToDb()
        geolocationDao.update(Geolocation(id = 1, query = "84.104.16.12", status = "success", country = "UK"))
        geolocationDao.update(Geolocation(id = 2, query = "18.81.3.97", status = "success", country = "FR"))

        val allItems = geolocationDao.getAllItems() // returns newest to oldest, via timestamp
        assertEquals(allItems[0], Geolocation(id = 2, query = "18.81.3.97", status = "success", country = "FR", timestamp = allItems[0].timestamp))
        assertEquals(allItems[1], Geolocation(id = 1, query = "84.104.16.12", status = "success", country = "UK", timestamp = allItems[1].timestamp))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        geolocationDao.delete(item1)
        geolocationDao.delete(item2)
        val allItems = geolocationDao.getAllItems()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = geolocationDao.getAllItems().first()
        assertEquals(item, item1)
    }

}