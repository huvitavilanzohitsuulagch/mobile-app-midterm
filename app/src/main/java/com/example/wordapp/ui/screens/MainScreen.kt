package com.example.wordapp.ui.view

import android.util.Log
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WordViewModel,
    onAddWordClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onEditWordClicked: (Int) -> Unit  // Хэрэв шаардлагатай бол ашиглагдах боловч энд бид toggle үйлдэл хийх болно.
) {
    val uiState = viewModel.uiState.collectAsState().value
    val currentWord = uiState.words.getOrNull(uiState.currentIndex)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(uiState.displayMode) {
        Log.d("DisplayModeState", "Current mode: ${uiState.displayMode}")
    }
    // Монгол үг харуулах эсэхийг удирдах хувьсагч
    var showMongolian by remember { mutableStateOf(uiState.displayMode != DisplayMode.FOREIGN_ONLY) }

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
                    // Гадаад үгийг харуулах
                    Text(
                        text = word.foreignWord,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(8.dp)
                    )

                    // Монгол үгийг харуулах хэсэг
                    when (uiState.displayMode) {
                        DisplayMode.FOREIGN_ONLY -> {
                            // Хэрэв зөвхөн гадаад үг харагдахаар тохирсон бол, товшсон үед нь нээж харуулах
                            if (!showMongolian) {
                                Text(
                                    text = "********",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            } else {
                                Text(
                                    text = word.mongolianWord,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        DisplayMode.MONGOLIAN_ONLY -> {
                            Text(
                                text = word.mongolianWord,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        DisplayMode.BOTH -> {
                            // Хэрэв хоёр хэл харуулах тохиргоо байгаа бол, Монгол үгийг товшсон үед showMongolian-г toggle хийхгүй,
                            // эсвэл уг хэсгийг өөр үйлдэлд ашиглаж болно.
                            Text(
                                text = word.mongolianWord,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
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

                    // Шинэчлэх болон Устгах товчнууд
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                // "Шинэчлэх" товч дарахад Монгол үгийн харагдах байдлыг toggle хийнэ.
                                showMongolian = !showMongolian
                            },
                            enabled = !uiState.isWordListEmpty
                        ) {
                            Text("Шинэчлэх")
                        }
                        Button(
                            onClick = {
                                // Устгахын өмнө баталгаажуулах диалог
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
