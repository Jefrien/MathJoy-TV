package dev.jefrien.mathjoytv.models

import kotlinx.serialization.Serializable

@Serializable
data class ExamResponse(
    val exercise: Exercise,
    val response: String
)
