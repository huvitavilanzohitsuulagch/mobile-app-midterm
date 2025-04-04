package com.example.wordapp

import WordViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import com.example.wordapp.data.datastore.DisplayMode
import com.example.wordapp.data.model.WordDatabase
import com.example.wordapp.data.reposity.WordRepository
import com.example.wordapp.ui.theme.WordAppTheme
import com.example.wordapp.ui.view.SettingsScreen
import com.example.wordapp.ui.viewmodel.WordViewModel

class SettingsActivity : ComponentActivity() {
    private val wordViewModel: WordViewModel by viewModels {
        val db = WordDatabase.getDatabase(applicationContext)
        val repo = WordRepository(db.wordDao())
        val settingsDataStore = com.example.wordapp.data.datastore.SettingsDataStore(applicationContext)
        WordViewModelFactory(repo, settingsDataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordAppTheme {
                SettingsScreen(wordViewModel) {
                    finish()
                }
            }
        }
    }
}
