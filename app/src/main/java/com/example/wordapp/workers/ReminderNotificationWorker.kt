package com.example.wordapp.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.wordapp.MainActivity
import com.example.wordapp.R

class ReminderNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val channelId = "word_reminder_channel"
        val manager = NotificationManagerCompat.from(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Word Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // Апп руу орох PendingIntent
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) // Ресурс файлаа үүсгэж, ic_notification.png эсвэл ic_notification.xml байх ёстой
            .setContentTitle("Үгсээ давтах цаг боллоо!")
            .setContentText("Өдөрт нэг удаа шинэ үгсээ шалгаарай.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Android 13 (API 33) болон дараас мэдэгдэл өгөх permission шаардлагатай тул зөвшөөрлийг шалгана.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Зөвшөөрөл олдоогүй бол мэдэгдэл өгөх үйлдлийг үлүүлж эсвэл өөр тохиргоог хийнэ.
                return
            }
        }

        manager.notify(1, notification)
    }
}
