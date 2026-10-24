package com.example.week5soal2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week5soal2.data.DataQuestion
import com.example.week5soal2.model.Question
import com.example.week5soal2.model.UserAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val allQuestions = DataQuestion.initialQuestions

    private val _activeQuestions = MutableStateFlow<List<Question>>(emptyList())
    val activeQuestions: StateFlow<List<Question>> = _activeQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption.asStateFlow()

    private val _userAnswers = MutableStateFlow<List<UserAnswer>>(emptyList())
    val userAnswers: StateFlow<List<UserAnswer>> = _userAnswers.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    private val _inputError = MutableStateFlow<String?>(null)
    val inputError: StateFlow<String?> = _inputError.asStateFlow()

    private val _isQuizStarted = MutableStateFlow(false)
    val isQuizStarted: StateFlow<Boolean> = _isQuizStarted.asStateFlow()

    fun startQuiz(countText: String) {
        val count = countText.toIntOrNull()

        if (count == null || count < 1 || count > allQuestions.size) {
            _inputError.value = "Maximum is ${allQuestions.size} questions"
            return
        }

        _inputError.value = null
        _activeQuestions.value = allQuestions.shuffled().take(count)
        _currentQuestionIndex.value = 0
        _userAnswers.value = emptyList()
        _selectedOption.value = null
        _isQuizFinished.value = false
        _isQuizStarted.value = true
    }

    fun selectOption(option: String) {
        _selectedOption.value = option
    }

    fun nextQuestion() {
        val currentQ = _activeQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        val selected = _selectedOption.value ?: return

        val answer = UserAnswer(
            question = currentQ,
            selectedAnswer = selected,
            isCorrect = selected == currentQ.correctAnswer
        )
        _userAnswers.value = _userAnswers.value + answer
        _selectedOption.value = null

        if (_currentQuestionIndex.value + 1 < _activeQuestions.value.size) {
            _currentQuestionIndex.value += 1
        } else {
            _isQuizFinished.value = true
        }
    }

    fun restartQuiz() {
        _isQuizStarted.value = false
        _isQuizFinished.value = false
    }
}