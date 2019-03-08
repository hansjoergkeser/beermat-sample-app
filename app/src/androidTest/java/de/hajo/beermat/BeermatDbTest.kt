package de.hajo.beermat

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import de.hajo.beermat.database.BeerDatabase
import de.hajo.beermat.database.BeermatDao
import de.hajo.beermat.model.Beermat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author hansjoerg.keser
 * @since 26.11.18
 */
class BeermatDbTest {

    private lateinit var beermatDao: BeermatDao
    private lateinit var db: BeerDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, BeerDatabase::class.java)
            .build()
        beermatDao = db.beerDao()
    }

    @After
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun addOneBeer() {
        beermatDao.insert(Beermat(0))
        val amountBeforeUpdate = beermatDao.getAmount()
        beermatDao.updateAmount(amountBeforeUpdate + 1)
        assertEquals(1, beermatDao.getAmount())
    }

    @Test
    fun removeOneBeer() {
        beermatDao.insert(Beermat(10))
        val amountBeforeUpdate = beermatDao.getAmount()
        beermatDao.updateAmount(amountBeforeUpdate - 1)
        assertEquals(9, beermatDao.getAmount())
    }

}