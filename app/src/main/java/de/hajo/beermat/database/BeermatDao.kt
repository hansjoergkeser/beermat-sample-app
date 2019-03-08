package de.hajo.beermat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.hajo.beermat.model.Beermat

/**
 * @author hansjoerg.keser
 * @since 23.11.18
 */
@Dao
interface BeermatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(beermat: Beermat)

    @Query("SELECT amount FROM beermat")
    fun getAmount(): Int

    @Query("SELECT price FROM beermat")
    fun getPrice(): Int

    @Query("SELECT total_price FROM beermat")
    fun getTotalPrice(): Int

    @Query("UPDATE beermat SET amount = :newAmount")
    fun updateAmount(newAmount: Int)

    @Query("UPDATE beermat SET price = :newPrice")
    fun updatePrice(newPrice: Int)

    @Query("UPDATE beermat SET total_price = :newTotalPrice")
    fun updateTotalPrice(newTotalPrice: Int)

    @Query("DELETE FROM beermat")
    fun deleteAll()

}