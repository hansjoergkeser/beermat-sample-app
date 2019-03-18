package de.hajo.beermat

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import de.hajo.beermat.events.BeermatCreationEvent
import de.hajo.beermat.events.BeermatGetAmountEvent
import de.hajo.beermat.events.BeermatGetEvent
import de.hajo.beermat.events.BeermatUpdateAmountEvent
import de.hajo.beermat.repository.BeerRepository
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.et_price
import kotlinx.android.synthetic.main.content_main.tv_beer_count
import kotlinx.android.synthetic.main.content_main.tv_total_price_of_line
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.text.NumberFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
		if (BuildConfig.DEBUG)
			Timber.plant(DebugTree())

		refresh()

		et_price.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				updatePrice()
			}
			false
		}

		fab.setOnClickListener {
			refresh()
		}

	}

	override fun onStart() {
		super.onStart()
		EventBus.getDefault().register(this)
	}

	override fun onPause() {
		EventBus.getDefault().unregister(this)
		super.onPause()
	}

	override fun onDestroy() {
		BeerRepository(this).closeDatabase()
		Timber.d("Closing BeerDatabase.")
		super.onDestroy()
	}

	private fun refresh() {
		BeerRepository(this).getBeermatState()
	}

	fun increaseBeerCount(view: View) {
		BeerRepository(this).getBeerAmount(true)
	}

	fun reduceBeerCount(view: View) {
		BeerRepository(this).getBeerAmount(false)
	}

	private fun updatePrice() {
		val beerCount = tv_beer_count.text.toString().toInt()
		val itemPriceInt = getItemPriceInt()
		BeerRepository(this).updateBeerPriceAndTotalPrice(itemPriceInt, calculateAndDisplayTotalPrice(beerCount, itemPriceInt))
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun handleUpdatedBeerCount(updateAmountEvent: BeermatUpdateAmountEvent) {
		val beerCount = updateAmountEvent.amountOfBeers
		tv_beer_count.text = beerCount.toString()

		val itemPriceInt = getItemPriceInt()
		val totalPrice = calculateAndDisplayTotalPrice(beerCount, itemPriceInt)

		BeerRepository(this).updateBeerPriceAndTotalPrice(itemPriceInt, totalPrice)

		if (updateAmountEvent.increasedCount) executeCheering(updateAmountEvent.amountOfBeers) else executeBullying(updateAmountEvent.amountOfBeers)
		Timber.d("handleUpdatedBeerCount() finished. Beermat table updated.")
	}

	private fun calculateAndDisplayTotalPrice(beerCount: Int, itemPriceInt: Int): Int {
		tv_total_price_of_line.text = NumberFormat.getCurrencyInstance(Locale.GERMANY)
				.format(beerCount.toDouble() * itemPriceInt.toDouble() / 100)

		return beerCount * itemPriceInt
	}

	private fun getItemPriceInt(): Int {
		val allButDigitsRegex = Regex("[^0-9]")
		return allButDigitsRegex.replace(et_price.text, "").toInt()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun handleCurrentBeerCount(getAmountEvent: BeermatGetAmountEvent) {
		var tmpCount = getAmountEvent.amountOfBeers
		if (getAmountEvent.increasedCount) tmpCount++ else if (!getAmountEvent.increasedCount && tmpCount == 0) tmpCount =
				0 else tmpCount--
		BeerRepository(this).updateBeerCount(tmpCount, getAmountEvent.increasedCount)
		Timber.d("handleCurrentBeerCount() finished. Beermat get action finished.")
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun handleBeermatCreation(creationEvent: BeermatCreationEvent) {
		tv_beer_count.text = "1"
		et_price.setText(R.string.default_price)
		calculateAndDisplayTotalPrice(1, 300)
		Timber.d("handleBeermatCreation() finished. Default beermat table has been created.")
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun handleBeermatGetEvent(getEvent: BeermatGetEvent) {
		if (getEvent.amountOfBeers == 0) {
			BeerRepository(this).createDefaultBeermat()
		} else {
			tv_beer_count.text = getEvent.amountOfBeers.toString()
			et_price.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(getEvent.price / 100.toDouble()))
			tv_total_price_of_line.text =
					NumberFormat.getCurrencyInstance(Locale.GERMANY).format(getEvent.totalPrice / 100.toDouble())
		}
		Timber.d("handleBeermatGetEvent() finished")
	}

	private fun executeCheering(beerCount: Int) {
		when (beerCount) {
			3 -> showSnackbarMessage("Your third beer now... Cheers.")
			6 -> showSnackbarMessage("Already 6 beers... Respeeect!")
			9 -> showSnackbarMessage("Woh-woh, easy fella...")
			12 -> showSnackbarMessage("You think you can handle this?!?")
			15 -> showSnackbarMessage("Go home, you're drunk...")
			18 -> showSnackbarMessage("...and don't text your ex!")
			21 -> showSnackbarMessage("Get therapy you drunkard...")
			24 -> showSnackbarMessage("...but thanks for using this app :-)")
			27 -> showSnackbarMessage("Ambulance has been called...")
			30 -> showSnackbarMessage("...just breathe and don't move!")
			33 -> showSnackbarMessage("You still can read this?!?")
			37 -> showSnackbarMessage("You're definitely one step away from a liver cirrhosis.")
		}
	}

	private fun executeBullying(beerCount: Int) {
		when (beerCount) {
			0 -> showSnackbarMessage("Booo")
			4 -> showSnackbarMessage("Good choice, listen to mama.")
			7 -> showSnackbarMessage("Too afraid huh?!?")
			10 -> showSnackbarMessage("And I thought you are brave.")
			13 -> showSnackbarMessage("Who stole your beer?!?")
			16 -> showSnackbarMessage("Good... but you're still drunk!")
			19 -> showSnackbarMessage("Guess you finally came to reason?")
		}
	}

	private fun showSnackbarMessage(shortText: String) {
		Snackbar.make(findViewById(android.R.id.content), shortText, Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
	}

}
