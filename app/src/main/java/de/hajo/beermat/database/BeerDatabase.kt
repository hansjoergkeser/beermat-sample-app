package de.hajo.beermat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.hajo.beermat.model.Beermat
import timber.log.Timber

const val DB_NAME = "beermat-database.db"

/**
 * @author hansjoerg.keser
 * @since 23.11.18
 */
@Database(entities = [Beermat::class], version = 1)
abstract class BeerDatabase : RoomDatabase() {
    abstract fun beerDao(): BeermatDao

    // https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
    companion object : SingletonHolder<BeerDatabase, Context>({
        Timber.i("Creating BeerDatabase.")
        Room.databaseBuilder(it.applicationContext, BeerDatabase::class.java, DB_NAME)
            // https://developer.android.com/training/data-storage/room/migrating-db-versions
            .fallbackToDestructiveMigration()
            .build()
    })

}
