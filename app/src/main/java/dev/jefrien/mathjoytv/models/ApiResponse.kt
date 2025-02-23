package dev.jefrien.mathjoytv.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val data: List<Exercise>
)
