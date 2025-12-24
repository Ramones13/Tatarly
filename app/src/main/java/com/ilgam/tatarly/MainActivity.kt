package com.ilgam.tatarly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilgam.tatarly.data.local.AppDatabase
import com.ilgam.tatarly.data.repository.WordRepository
import com.ilgam.tatarly.presentation.WordViewModel
import com.ilgam.tatarly.ui.theme.TatarlyTheme

class MainActivity : ComponentActivity() {

    // Временный ViewModelProvider (позже заменим на Hilt)
    private val viewModel: WordViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(applicationContext)
                val repository = WordRepository(database.wordDao())
                return WordViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TatarlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordListScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun WordListScreen(viewModel: WordViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Словарь татарского языка",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Кнопка добавления тестового слова
        Button(
            onClick = {
                viewModel.addWord("китап", "книга", "Основные")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить")
            Spacer(Modifier.width(8.dp))
            Text("Добавить слово 'китап'")
        }

        Spacer(Modifier.height(16.dp))

        // Статистика
        val learnedCount = uiState.words.count { it.isLearned }
        Text(
            text = "Всего слов: ${uiState.words.size}, изучено: $learnedCount",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        // Список слов
        if (uiState.words.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Список слов пуст. Нажмите кнопку выше, чтобы добавить.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.words) { word ->
                    WordCard(word = word, viewModel = viewModel)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordCard(word: com.ilgam.tatarly.data.local.entity.WordEntity, viewModel: WordViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (word.isLearned)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = word.tatWord,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = word.rusWord,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Категория: ${word.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                // Кнопка "Изучено"
                IconButton(
                    onClick = { viewModel.markAsLearned(word.id, !word.isLearned) }
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Изучено",
                        tint = if (word.isLearned) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                }

                // Кнопка удаления
                IconButton(
                    onClick = { viewModel.deleteWord(word) }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}