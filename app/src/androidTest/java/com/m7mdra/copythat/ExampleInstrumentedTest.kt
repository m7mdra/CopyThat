package com.m7mdra.copythat


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m7mdra.copythat.database.ClipDatabase
import com.m7mdra.copythat.database.ClipEntry
import com.m7mdra.copythat.database.ClipEntryDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var db: ClipDatabase
    private lateinit var dao: ClipEntryDao
    private val clipEntry = ClipEntry.empty()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ClipDatabase::class.java)
            .build()
        dao = db.dao()
    }

    @Test
    fun testInsertNewClipEntry() {
        dao.insert(clipEntry)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertNoValues()

    }

    @Test
    fun testToggleLike() {
        dao.toggleFavorite(clipEntry)
            .test()

            .assertComplete()
            .assertNoErrors()

    }

    @Test
    fun insertClipAndGet() {
        dao.insert(clipEntry).blockingAwait()
        dao.findClipById(clipEntry.id)
            .test()
            .assertValue {
                it.id == clipEntry.id
            }
    }

    @Test
    fun findClipWhenItsNotInserted() {
        dao.findClipById(1)
            .test()
            .assertNoValues()

    }
    @Test
    fun testDeleteAll(){
        dao.insert(clipEntry).blockingAwait()

        dao.deleteAll().blockingAwait()
        dao.getEntries()
            .test()
            .assertNoValues()
    }
    @Test
    fun getAllClipEntries() {
        for (i in 1..10) {
            dao.insert(ClipEntry(i, "", 0L, "", 0)).blockingAwait()
        }
        dao.getEntries()
            .test()
            .assertValue {
                it.isNotEmpty()
            }
    }

    @After
    fun tearDown() {
        db.close()
    }

}
