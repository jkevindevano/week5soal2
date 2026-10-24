package com.example.week5soal2.model

data class Question(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)

data class UserAnswer(
    val question: Question,
    val selectedAnswer: String,
    val isCorrect: Boolean
)