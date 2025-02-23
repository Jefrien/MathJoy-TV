package dev.jefrien.mathjoytv.models

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val exercise: String,
    val answers: List<String>,
    val correct: String
)
