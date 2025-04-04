package com.example.wordapp

import WordViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordapp.data.datastore.SettingsDataStore
import com.example.wordapp.data.model.WordDatabase
import com.example.wordapp.data.reposity.WordRepository
import com.example.wordapp.ui.theme.WordAppTheme
import com.example.wordapp.ui.view.MainScreen
import com.example.wordapp.ui.viewmodel.WordViewModel
import com.example.wordapp.workers.ReminderNotificationWorker
import java.util.concurrent.TimeUnit  // Зөв import

class MainActivity : ComponentActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        val db = WordDatabase.getDatabase(applicationContext)
        val repo = WordRepository(db.wordDao())
        val settingsDataStore = SettingsDataStore(applicationContext)
        WordViewModelFactory(repo, settingsDataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WorkManager-ийн periodic work request-ийн хамгийн бага давталтын хугацаа нь 15 минут.
        // Тестийн зориулалтаар та үүнийг өөрчлөхийг оролдсон бол алдаа гарна.
        val workRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "reminder_notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
        setContent {
            WordAppTheme {
                MainScreen(
                    viewModel = wordViewModel,
                    onAddWordClicked = {
                        // 2-р цонх руу орох (Add/Edit дэлгэц)
                        startActivity(Intent(this, AddEditWordActivity::class.java))
                    },
                    onSettingsClicked = {
                        // Тохиргооны цонх руу
                        startActivity(Intent(this, SettingsActivity::class.java))
                    },
                    onEditWordClicked = { wordId ->
                        // Засах дэлгэц рүү (өнөөх үгийн id-г дамжуулах)
                        val intent = Intent(this, AddEditWordActivity::class.java)
                        intent.putExtra("WORD_ID", wordId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
