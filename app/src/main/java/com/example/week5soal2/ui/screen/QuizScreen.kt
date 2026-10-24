package com.example.week5soal2.ui.screen

import android.util.Log.e
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week5soal2.viewmodel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel = viewModel()) {
    val isStarted by viewModel.isQuizStarted.collectAsState()
    val isFinished by viewModel.isQuizFinished.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (!isStarted) {
            StartQuizContent(viewModel = viewModel)
        } else if (isFinished) {
            ResultQuizContent(viewModel = viewModel)
        } else {
            QuestionContent(viewModel = viewModel)
        }
    }
}

@Composable
fun StartQuizContent(viewModel: QuizViewModel) {
    var countInput by remember { mutableStateOf("") }
    val inputError by viewModel.inputError.collectAsState()

    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Mini Quiz", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                "How many questions do you want? (1-5)",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = countInput,
                onValueChange = { countInput = it },
                label = { Text("Number of questions", color = Color.Gray) },
                isError = inputError != null,
                supportingText = {
                    if (inputError != null) {
                        Text(inputError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.startQuiz(countInput) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A5568))
            ) {
                Text("Start Quiz", color = Color.White)
            }
        }
    }
}

@Composable
fun QuestionContent(viewModel: QuizViewModel) {
    val questions by viewModel.activeQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()

    val currentQuestion = questions.getOrNull(currentIndex) ?: return

    Box(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Question ${currentIndex + 1}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                currentQuestion.questionText,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            currentQuestion.options.forEach { option ->
                val isSelected = selectedOption == option
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF4A5A90) else Color(0xFFECEFF1)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { viewModel.selectOption(option) }
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.nextQuestion() },
                enabled = selectedOption != null,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A5A90),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text("Next", color = Color.White)
            }
        }
    }
}

@Composable
fun ResultQuizContent(viewModel: QuizViewModel) {
    val userAnswers by viewModel.userAnswers.collectAsState()
    val totalQuestions = userAnswers.size
    val score = userAnswers.count { it.isCorrect }
    val wrongAnswers = userAnswers.filter { !it.isCorrect }

    Box () {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Quiz Finished!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                "Your Score: $score / $totalQuestions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            if (wrongAnswers.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Perfect! No wrong answers.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(wrongAnswers) { item ->
                        Column {
                            Text(
                                "Q${item.question.id}: ${item.question.questionText}",
                                fontWeight = FontWeight.Bold
                            )
                            Text("Your Answer: ${item.selectedAnswer}", color = Color.Red, fontSize = 13.sp)
                            Text("Correct Answer: ${item.question.correctAnswer}", color = Color.Gray, fontSize = 13.sp)
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.restartQuiz() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A5A90))
            ) {
                Text("Restart Quiz", color = Color.White)
            }
        }
    }
}