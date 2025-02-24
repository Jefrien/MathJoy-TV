package dev.jefrien.mathjoytv

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.tv.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import dev.jefrien.mathjoytv.db.AppDatabase
import dev.jefrien.mathjoytv.db.entities.ExamEntity
import dev.jefrien.mathjoytv.models.ExamResponse
import dev.jefrien.mathjoytv.models.Exercise
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import dev.jefrien.mathjoytv.ui.theme.TealDark
import dev.jefrien.mathjoytv.ui.theme.TealDarker
import dev.jefrien.mathjoytv.ui.theme.TealLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class Exam : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val exercises =
            Json.decodeFromString<List<Exercise>>(intent.getStringExtra("exercises").toString())

        val title = intent.getStringExtra("title")
        setContent {
            MathJoyTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), shape = RectangleShape
                ) {
                    ExamApp(exercises, title.toString())
                }
            }
        }
    }
}

@Composable
fun ExerciseView(value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            "Cuanto es",
            fontSize = 16.sp,
            color = TealLight,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            value,
            fontSize = 66.sp,
            color = TealLight
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun OptionsView(options: List<String>, onAnswerClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        for (option in options) {
            item {
                Button(
                    onClick = { onAnswerClick(option) },
                    shape = ButtonDefaults.shape(
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    colors = ButtonDefaults.colors(
                        containerColor = TealLight,
                        contentColor = TealDark,
                        focusedContainerColor = TealDarker,
                        focusedContentColor = TealLight
                    ),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(option, fontSize = 45.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RowApp(exercise: Exercise, onAnswerClick: (iem: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // Esto hace que ocupe 50% del espacio
                .fillMaxHeight()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ExerciseView(exercise.exercise)
        }
        Box(
            modifier = Modifier
                .weight(1f) // Esto hace que ocupe 50% del espacio
                .fillMaxHeight()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            OptionsView(exercise.answers, onAnswerClick)
        }
    }
}

@Composable
fun ExamApp(exercises: List<Exercise>, title: String) {
    var context = LocalContext.current
    var index by remember { mutableStateOf(0) }
    var exercise by remember { mutableStateOf(exercises.get(index)) }
    val responses = remember { mutableStateListOf<ExamResponse>() }
    var progress by remember { mutableStateOf(0f) }

    fun saveResponses() {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "mathjoy_db"
        ).build()

        val examDao = db.examDao()


        val responsesSimple: MutableList<String> = mutableListOf()
        for (resp in responses) {
            responsesSimple.add("${resp.exercise.exercise} = ${resp.response}")
        }
        examDao.insertAll(
            ExamEntity(
                studentName = "Carlos",
                responses = Json.encodeToString(responsesSimple)
            )
        )
    }

    fun calculateProgress() {
        val p = (index * 100) / exercises.size
        progress = p.toFloat() / 100
    }

    fun handleOptionClick(option: String) {
        val item = ExamResponse(
            exercise = exercises.get(index),
            response = option
        )
        responses.add(item)
        index++
        calculateProgress()

        if (index >= exercises.size) {

            CoroutineScope(Dispatchers.IO).launch {
                saveResponses()

                val intent = Intent(context, Results::class.java)
                intent.putExtra("responses", Json.encodeToString(responses.toList()))
                context.startActivity(intent)
            }
        } else {

            exercise = exercises.get(index)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            trackColor = TealLight,
            color = TealDarker
        )
        Logo()
        Text(
            text = "Prueba de $title",
            color = Color.White,
            fontSize = 24.sp
        )
        RowApp(exercise, { option -> handleOptionClick(option) })
    }
}
