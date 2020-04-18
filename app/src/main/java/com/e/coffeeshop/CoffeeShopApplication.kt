package com.e.coffeeshop

import android.app.Application
import timber.log.Timber

class CoffeeShopApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}