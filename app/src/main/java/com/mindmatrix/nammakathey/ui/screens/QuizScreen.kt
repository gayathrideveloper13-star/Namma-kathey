package com.mindmatrix.nammakathey.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.nammakathey.Hero
import com.mindmatrix.nammakathey.data.AppDatabase
import com.mindmatrix.nammakathey.data.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListScreen(
    heroes: List<Hero>,
    onStartQuiz: (Hero) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interactive Quizzes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE0F7FA))
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select a hero to test your knowledge!", fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(heroes) { hero ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onStartQuiz(hero) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = hero.name_en, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "District: ${hero.district}", fontSize = 14.sp, color = Color.Gray)
                        }
                        Button(onClick = { onStartQuiz(hero) }) {
                            Text("Start")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveQuizScreen(
    hero: Hero,
    onQuizFinished: () -> Unit
) {
    val questions = hero.quiz
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)
    val sessionManager = remember { SessionManager(context) }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available for this hero.")
        }
        return
    }

    if (isFinished) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Quiz Completed!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Score: $score / ${questions.size}", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onQuizFinished) {
                Text("Back to Quizzes")
            }
        }
    } else {
        val currentQuestion = questions[currentQuestionIndex]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentQuestion.question_en,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            currentQuestion.options_en.forEachIndexed { index, option ->
                Button(
                    onClick = {
                        if (index == currentQuestion.correct_index) {
                            score++
                        }
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            isFinished = true
                            // Save to database
                            scope.launch {
                                val userId = sessionManager.getLoggedInUserId()
                                if (userId != -1L) {
                                    db.userDao().updateQuizStats(userId, score)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = option, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
