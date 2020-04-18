package com.e.coffeeshop

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.e.coffeeshop.databinding.ActivityMainBinding
import timber.log.Timber

const val KEY_PRODUCTS_SOLD = "key_products_sold"
const val KEY_TOTAL_COST = "key_total_cost"
const val KEY_TIMER_SECONDS = "key_timer_seconds"

class MainActivity : AppCompatActivity() {

    private var numberOfProductsSold = 0
    private var totalCost = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: BeverageTimer

    // Beverages to be sold. The "startProductionAmount" means that a user will be able to
    // purchase that product after they've purchased that amount of products.
    data class Beverage(val imageId: Int, val price: Int, val startProductionAmount: Int)

    private val allBeverages: List<Beverage> = listOf(
        Beverage(R.drawable.drip2, 2, 0),
        Beverage(R.drawable.cappuccino, 4, 10),
        Beverage(R.drawable.espresso2, 3, 20),
        Beverage(R.drawable.tea2, 2, 30),
        Beverage(R.drawable.togo2, 5, 50)
    )

    // Set the initial beverage to be displayed
    private var currentBeverage = allBeverages[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate was called")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        timer = BeverageTimer(this.lifecycle)

        if (savedInstanceState != null) {
            numberOfProductsSold = savedInstanceState.getInt(KEY_PRODUCTS_SOLD)
            totalCost = savedInstanceState.getInt(KEY_TOTAL_COST)
            timer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS)
        }

        // Assign initial values to labels
        binding.itemsSold = numberOfProductsSold
        binding.totalSold = totalCost
        binding.productButton.setBackgroundResource(currentBeverage.imageId)

        binding.productButton.setOnClickListener {
            productButtonClicked()
        }
    }

    private fun productButtonClicked() {
        totalCost += currentBeverage.price
        numberOfProductsSold++
        binding.itemsSold = numberOfProductsSold
        binding.totalSold = totalCost

        setCurrentBeverage()
    }

    private fun setCurrentBeverage() {
        var newBeverage = allBeverages[0]

        // Loop through all beverages. Once the numberOfProductsSold is equal to or greater
        // than the startProductionAmount of the next beverage, advance the newBeverage to the nextBeverage in the list.
        for (beverage in allBeverages) {
            if (numberOfProductsSold >= beverage.startProductionAmount) {
                newBeverage = beverage
            } else break
        }

        // But only assign that new beverage to the current if it's a 'new' and different beverage
        if (newBeverage != currentBeverage) {
            currentBeverage = newBeverage
            binding.productButton.setBackgroundResource(newBeverage.imageId)
        }
    }

    // Share Menu
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_message, numberOfProductsSold, totalCost))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (exeption: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuItem -> onShare()
         }
        return super.onOptionsItemSelected(item)
    }


    // LIFECYCLE CALLBACK METHODS

    override fun onStart() {
        super.onStart()
        timer.startTimer()
        Timber.i("onStart was called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart was called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume was called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState was called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause was called")
    }

    override fun onStop() {
        super.onStop()
        timer.stopTimer()
        Timber.i("onStop was called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PRODUCTS_SOLD, numberOfProductsSold)
        outState.putInt(KEY_TOTAL_COST, totalCost)
        outState.putInt(KEY_TIMER_SECONDS, timer.secondsCount)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy was called")
    }
}

