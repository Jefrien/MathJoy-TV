package dev.jefrien.mathjoytv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import dev.jefrien.mathjoytv.models.ExamResponse
import dev.jefrien.mathjoytv.models.Exercise
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import dev.jefrien.mathjoytv.ui.theme.ResponseCorrect
import dev.jefrien.mathjoytv.ui.theme.ResponseError
import kotlinx.serialization.json.Json

class Results : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val responses =
            Json.decodeFromString<List<ExamResponse>>(intent.getStringExtra("responses").toString())

        setContent {
            MathJoyTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), shape = RectangleShape
                ) {
                    ResultsApp(responses)
                }
            }
        }
    }
}

@Composable
fun ResultItem(number: Int, exercise: String, response: String, correct: String) {

    fun getColor(): Color {
        return if (response == correct) {
            ResponseCorrect
        } else {
            ResponseError
        }
    }

    Box(
        modifier = Modifier.background(getColor()),
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                "$number) ",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
            Column {
                Text("$exercise = $response", color = Color.White, fontSize = 25.sp)
                Text("Respuesta Correcta: $correct", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ResponsesGrid(responses: List<ExamResponse>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(start = 40.dp, end = 40.dp)
    ) {
        for((index, value ) in responses.withIndex()) {
            val indexSum = index + 1
            item { ResultItem(
                indexSum,
                value.exercise.exercise,
                value.response,
                value.exercise.correct
            ) }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ResultsApp(responses: List<ExamResponse>) {
    val context = LocalContext.current
    Button (
        onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        },
        modifier = Modifier.padding(16.dp) // Añade un poco de espacio alrededor del botón
    ) {
        Text("Ir al inicio", color = Color.White)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Text(
            text = "Estos son tus resultados",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        ResponsesGrid(responses)
    }
}
