package com.example.wordapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wordapp.data.datastore.DisplayMode
import com.example.wordapp.ui.viewmodel.WordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: WordViewModel, onClose: () -> Unit) {
    // uiState-г state delegation-аар авч байна
    val uiState by viewModel.uiState.collectAsState()
    // Анхийн утга нь viewModel-ийн state-аас авч үүсгэгдэнэ
    var selectedMode by remember { mutableStateOf(uiState.displayMode) }

    // uiState.displayMode өөрчлөгдөх үед local state-г шинэчилнэ
    LaunchedEffect(uiState.displayMode) {
        selectedMode = uiState.displayMode
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тохиргоо") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Үг харагдах тохиргоо", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // 3 сонголт
            DisplayMode.values().forEach { mode ->
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    RadioButton(
                        selected = (selectedMode == mode),
                        onClick = { selectedMode = mode }
                    )
                    Text(mode.name)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.setDisplayMode(selectedMode)
                    onClose()
                }
            ) {
                Text("Хадгалах")
            }
        }
    }
}
