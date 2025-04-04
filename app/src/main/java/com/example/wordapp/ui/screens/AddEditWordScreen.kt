package com.example.wordapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wordapp.data.model.Word
import com.example.wordapp.ui.viewmodel.WordViewModel
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWordScreen(
    viewModel: WordViewModel,
    wordId: Int,
    onDone: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var foreignText by remember { mutableStateOf("") }
    var mongolianText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = wordId) {
        if (wordId != -1) {
            // getWordById-г дуудаж, үр дүнг textfield-үүдэд prefill хийж байна.
            val word = viewModel.getWordById(wordId)
            word?.let {
                foreignText = it.foreignWord
                mongolianText = it.mongolianWord
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (wordId == -1) "Add Word" else "Edit Word") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = foreignText,
                onValueChange = { foreignText = it },
                label = { Text("Foreign Word") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mongolianText,
                onValueChange = { mongolianText = it },
                label = { Text("Mongolian Word") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    scope.launch {
                        if (wordId == -1) {
                            // Шинээр үг нэмэх үед хоёр талбарын утгыг ашиглана.
                            viewModel.insertWord(Word(foreignWord = foreignText, mongolianWord = mongolianText))
                        } else {
                            // Засах үйлдэл: getWordById-оор өгөгдлийн сангаас тухайн үгийг авч шинэчилнэ.
                            viewModel.getWordById(wordId)?.let { existing ->
                                val updated = existing.copy(foreignWord = foreignText, mongolianWord = mongolianText)
                                viewModel.updateWord(updated)
                            }
                        }
                        onDone()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Хадгалах")
            }
        }
    }
}
