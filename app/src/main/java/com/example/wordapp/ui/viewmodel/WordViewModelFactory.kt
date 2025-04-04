import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordapp.data.datastore.SettingsDataStore
import com.example.wordapp.data.reposity.WordRepository
import com.example.wordapp.ui.viewmodel.WordViewModel

class WordViewModelFactory(
    private val repository: WordRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository, settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
