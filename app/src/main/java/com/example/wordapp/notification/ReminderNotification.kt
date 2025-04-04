package com.example.vocabapp.notifications

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // TODO: Мэдэгдэл үүсгэж, NotificationManager ашиглан илгээх кодыг бичнэ.
        return Result.success()
    }
}

class NotificationScheduler(private val context: Context) {
    private val handler = Handler(Looper.getMainLooper())
    private val notificationRunnable = object : Runnable {
        override fun run() {
            // Мэдэгдэл гаргах үйлдлийг энд бичнэ
            showNotification()
            // 30 секунд дараа дахин дахин ажиллуулах
            handler.postDelayed(this, 30 * 1000)
        }
    }

    fun start() {
        handler.post(notificationRunnable)
    }

    fun stop() {
        handler.removeCallbacks(notificationRunnable)
    }

    private fun showNotification() {
        // Энд мэдэгдлийг үүсгэж харуулах кодыг бичнэ
    }
}
