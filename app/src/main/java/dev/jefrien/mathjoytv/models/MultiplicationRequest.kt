package dev.jefrien.mathjoytv.models

import kotlinx.serialization.Serializable

@Serializable
data class MultiplicationRequest(
    val action: String,
    val difficulty: String,
    val quantity: Int,
    val table: String,
    val password: String
)
