/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.FerramentasDatabase
import com.example.inventory.data.Ferramenta
import com.example.inventory.data.FerramentaDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FerramentaDaoTest {

    private lateinit var ferramentaDao: FerramentaDao
    private lateinit var ferramentasDatabase: FerramentasDatabase
    private val ferramenta1 = Ferramenta(1, "Martelos", "martelo", "martelo", quantidade = 30, data_emprestimo = "10/10/2020")
    private val ferramenta2 = Ferramenta(2, "Chave de fenda", "chaves de fenda", "chave de fenda", quantidade = 30, data_emprestimo = "20/10/2024")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        ferramentasDatabase = Room.inMemoryDatabaseBuilder(context, FerramentasDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        ferramentaDao = ferramentasDatabase.FerramentaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        ferramentasDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = ferramentaDao.getAllFerramentas().first()
        assertEquals(allItems[0], ferramenta1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllFerramentasFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = ferramentaDao.getAllFerramentas().first()
        assertEquals(allItems[0], ferramenta1)
        assertEquals(allItems[1], ferramenta2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsFerramentasFromDB() = runBlocking {
        addOneItemToDb()
        val item = ferramentaDao.getFerramentas(1)
        assertEquals(item.first(), ferramenta1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        ferramentaDao.delete(ferramenta1)
        ferramentaDao.delete(ferramenta2)
        val allItems = ferramentaDao.getAllFerramentas().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemsToDb()
        ferramentaDao.update(Ferramenta(1, "Martelo", "martelos", "martelo", data_emprestimo = "10/10/204", quantidade = 40))
        ferramentaDao.update(Ferramenta(2, "Chave de fenda", "chaves de fenda", "chave de fenda", quantidade = 20, data_emprestimo = "20/10/2024"))

        val allItems = ferramentaDao.getAllFerramentas().first()
        assertEquals(allItems[0], Ferramenta(1, "Martelo", "martelos", "martelo", quantidade = 40, data_emprestimo = "10/10/2024"))
        assertEquals(allItems[1], Ferramenta(2, "Chave de fenda", "chaves de fenda", "chave de fenda", quantidade = 20, data_emprestimo = "20/10/2024"))
    }

    private suspend fun addOneItemToDb() {
        ferramentaDao.insert(ferramenta1)
    }

    private suspend fun addTwoItemsToDb() {
        ferramentaDao.insert(ferramenta1)
        ferramentaDao.insert(ferramenta2)
    }
}
