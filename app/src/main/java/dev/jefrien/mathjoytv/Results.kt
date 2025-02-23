package dev.jefrien.mathjoytv

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.tv.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import dev.jefrien.mathjoytv.models.ExamResponse
import dev.jefrien.mathjoytv.models.Exercise
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import dev.jefrien.mathjoytv.ui.theme.TealDark
import dev.jefrien.mathjoytv.ui.theme.TealDarker
import dev.jefrien.mathjoytv.ui.theme.TealLight
import kotlinx.serialization.json.Json

class Results : ComponentActivity() {
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
                    ResultsApp()
                }
            }
        }
    }
}

@Composable
fun ResultsApp() {


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
            fontSize = 24.sp
        )
    }
}
