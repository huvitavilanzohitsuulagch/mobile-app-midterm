package com.example.wordapp.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wordapp.data.datastore.DisplayMode
import com.example.wordapp.ui.viewmodel.WordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: WordViewModel,
    onAddWordClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onEditWordClicked: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val currentWord = uiState.words.getOrNull(uiState.currentIndex)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Log display mode for debugging
    LaunchedEffect(uiState.displayMode) {
        println("Display mode: ${uiState.displayMode}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word App") },
                actions = {
                    IconButton(onClick = onSettingsClicked) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWordClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Word")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isWordListEmpty) {
                Text("Одоогоор үг байхгүй байна.")
            } else {
                currentWord?.let { word ->
                    when (uiState.displayMode) {
                        DisplayMode.BOTH -> {
                            // Хоёр хэл: Гадаад болон Монгол үгийг энгийн харуулах
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { onEditWordClicked(word.id) },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ) {
                            Text(
                                text = word.foreignWord,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                            }
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { onEditWordClicked(word.id) },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ){

                            Text(
                                text = word.mongolianWord,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                            }
                        }
                        DisplayMode.FOREIGN_ONLY -> {
                            // Гадаад үг үргэлж харуулах
                            var isForeignRevealed by remember(currentWord) { mutableStateOf(false) }
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { onEditWordClicked(word.id) },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ) {
                                Text(
                                    text = word.foreignWord,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            // Монгол үгийг "****" гэж нуугдаж харуулах, дарсан үед ил гарч өөрчлөгдөх
                            var isMongolianRevealed by remember(currentWord) { mutableStateOf(false) }
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { isMongolianRevealed = !isMongolianRevealed },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ) {
                                Text(
                                    text = if (isMongolianRevealed) word.mongolianWord else "****",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        DisplayMode.MONGOLIAN_ONLY -> {
                            // Монгол үг үргэлж харуулах

                            // Гадаад үгийг "****" гэж нуугдаж харуулах, дарсан үед ил гарч өөрчлөгдөх
                            var isForeignRevealed by remember(currentWord) { mutableStateOf(false) }
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { isForeignRevealed = !isForeignRevealed },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ) {
                                Text(
                                    text = if (isForeignRevealed) word.foreignWord else "****",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Box(
                                modifier = Modifier.combinedClickable(
                                    onClick = { onEditWordClicked(word.id) },
                                    onLongClick = { onEditWordClicked(word.id) }
                                )
                            ) {
                                Text(
                                    text = word.mongolianWord,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Шилжүүлэх товчнууд
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.previousWord() },
                            enabled = uiState.words.isNotEmpty() && uiState.currentIndex > 0
                        ) {
                            Text("Өмнөх")
                        }
                        Button(
                            onClick = { viewModel.nextWord() },
                            enabled = uiState.words.isNotEmpty() && uiState.currentIndex < uiState.words.size - 1
                        ) {
                            Text("Дараах")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Засах болон Устгах товчнууд
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onEditWordClicked(word.id) },
                            enabled = !uiState.isWordListEmpty
                        ) {
                            Text("Засах")
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Устгах уу?",
                                        actionLabel = "Тийм"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.deleteCurrentWord()
                                    }
                                }
                            },
                            enabled = !uiState.isWordListEmpty
                        ) {
                            Text("Устгах")
                        }
                    }
                }
            }
        }
    }
}
