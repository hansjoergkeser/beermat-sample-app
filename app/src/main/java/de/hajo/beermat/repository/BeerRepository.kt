package de.hajo.beermat.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import de.hajo.beermat.database.BeerDatabase
import de.hajo.beermat.events.BeermatCreationEvent
import de.hajo.beermat.events.BeermatGetEvent
import de.hajo.beermat.events.BeermatInitialGetEvent
import de.hajo.beermat.events.BeermatRefreshEvent
import de.hajo.beermat.events.BeermatUpdateEvent
import de.hajo.beermat.model.Beermat
import org.greenrobot.eventbus.EventBus

/**
 * @author hansjoerg.keser
 * @since 29.11.18
 */
class BeerRepository(val context: Context) {

    fun getBeerDatabase() = BeerDatabase.getInstance(context)

    fun closeDatabase() {
        getBeerDatabase().close()
    }

    @SuppressLint("StaticFieldLeak")
    fun createDefaultBeermat() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                getBeerDatabase().beerDao().deleteAll()
                getBeerDatabase().beerDao().insert(Beermat(1, 300, 300))
                EventBus.getDefault().post(BeermatCreationEvent())
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun getInitialBeermatState() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                val amountOfBeers = getBeerDatabase().beerDao().getAmount()
                val price = getBeerDatabase().beerDao().getPrice()
                val totalPrice = getBeerDatabase().beerDao().getTotalPrice()
                EventBus.getDefault().post(BeermatInitialGetEvent(amountOfBeers, price, totalPrice))
                return null
            }
        }.execute()
    }

	@SuppressLint("StaticFieldLeak")
	fun refresh() {
		object : AsyncTask<Void, Void, Void>() {
			override fun doInBackground(vararg voids: Void): Void? {
				val amountOfBeers = getBeerDatabase().beerDao().getAmount()
				EventBus.getDefault().post(BeermatRefreshEvent(amountOfBeers))
				return null
			}
		}.execute()
	}

    @SuppressLint("StaticFieldLeak")
    fun getBeerAmount(increasedCount: Boolean) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                val amountOfBeers = getBeerDatabase().beerDao().getAmount()
                EventBus.getDefault().post(BeermatGetEvent(amountOfBeers, increasedCount))
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateBeerCount(newCount: Int, increasedCount: Boolean) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                getBeerDatabase().beerDao().updateAmount(newCount)
                EventBus.getDefault().post(BeermatUpdateEvent(newCount, increasedCount))
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateBeerPriceAndTotalPrice(newPrice: Int, newTotalPrice: Int) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                getBeerDatabase().beerDao().updatePrice(newPrice)
                getBeerDatabase().beerDao().updateTotalPrice(newTotalPrice)
                return null
            }
        }.execute()
    }

}