package com.example.wordapp

import WordViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import com.example.wordapp.data.datastore.SettingsDataStore
import com.example.wordapp.data.model.WordDatabase
import com.example.wordapp.data.reposity.WordRepository
import com.example.wordapp.ui.theme.WordAppTheme
import com.example.wordapp.ui.view.AddEditWordScreen
import com.example.wordapp.ui.viewmodel.WordViewModel

class AddEditWordActivity : ComponentActivity() {
    private val wordViewModel: WordViewModel by viewModels {
        val db = WordDatabase.getDatabase(applicationContext)
        val repo = WordRepository(db.wordDao())
        val settingsDataStore = SettingsDataStore(applicationContext)
        WordViewModelFactory(repo, settingsDataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wordId = intent.getIntExtra("WORD_ID", -1)

        setContent {
            WordAppTheme {
                AddEditWordScreen(
                    viewModel = wordViewModel,
                    wordId = wordId,
                    onDone = { finish() }
                )
            }
        }
    }
}
