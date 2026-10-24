package com.example.week5soal2.data

import com.example.week5soal2.model.Question

object DataQuestion {
    val initialQuestions = listOf(
        Question(
            id = 1,
            questionText = "Ibu kota di Indonesia?",
            options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
            correctAnswer = "Jakarta"
        ),
        Question(
            id = 2,
            questionText = "Apakah mata kuliah VP susah?",
            options = listOf("Sangat Susah", "Sulit", "Sangat Mudah", "Biasa Saja"),
            correctAnswer = "Sulit"
        ),
        Question(
            id = 3,
            questionText = "Natal dirayakan pada bulan?",
            options = listOf("Agustus", "September", "November", "Desember"),
            correctAnswer = "Desember"
        ),
        Question(
            id = 4,
            questionText = "Ada berapa hari dalam seminggu?",
            options = listOf("5", "6", "7", "8"),
            correctAnswer = "7"
        ),
        Question(
            id = 5,
            questionText = "Mata Kuliah VP pakai apa?",
            options = listOf("Word", "Excel", "Android Studio", "VS Code"),
            correctAnswer = "Android Studio"
        )
    )
}