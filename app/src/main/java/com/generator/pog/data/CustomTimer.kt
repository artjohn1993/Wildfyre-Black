package com.generator.pog.data

import android.os.CountDownTimer
import kotlinx.coroutines.CompletionHandler

class CustomTimer(completionHandler: () -> Unit) {

    val timer = object : CountDownTimer(10 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            completionHandler.invoke()
        }
    }

    fun start() {
        timer.start()
    }

    fun cancel() {
        timer.cancel()
    }
}