package com.e.coffeeshop

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber


class BeverageTimer(lifecycle: Lifecycle): LifecycleObserver {

    var secondsCount = 0

    init {
        lifecycle.addObserver(this)
    }

    /*
    * Handler is a class meant to process a queue of android.os.Message[s]
    * or actions known as "Runnable[s]"
    */
    private var handler = Handler()
    private lateinit var runnable: Runnable

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {
        runnable = Runnable {
            secondsCount++
            Timber.i("Timer is at: $secondsCount")
            handler.postDelayed(runnable, 1000)
        }
        // This is what initially starts the timer apparently.
        handler.postDelayed(runnable, 1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {
        handler.removeCallbacks(runnable)
    }
}