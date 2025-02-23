package dev.jefrien.mathjoytv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.content
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Multiply : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathJoyTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), shape = RectangleShape
                ) {
                    MultiplyApp()
                }
            }
        }
    }
}

@Serializable
data class MultiplicationRequest(
    val action: String,
    val difficulty: String,
    val quantity: Int,
    val table: String,
    val password: String
)

data class Exercise(
    val exercise: String,
    val answers: List<String>,
    val correct: String
)

data class MultiplicationResponse(
    val data: List<Exercise>
)


@OptIn(InternalAPI::class)
suspend fun getRandomExercises(table: Int): MultiplicationResponse {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val response: HttpResponse = client.post(urlString = "https://jefrien-bot.jefrienalvizures.workers.dev/") {
        contentType(ContentType.Application.Json)
        setBody(MultiplicationRequest(
            action = "generateMultiplications",
            difficulty = "faciles de 2 cifras",
            quantity = 10,
            table = table.toString(),
            password = "passhere"
        ))
    }

    val result: MultiplicationResponse = response.body()

    return result
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonTable(text: String = "Button", onClick: () -> Unit) {
    Button(
        onClick, modifier = Modifier
            .height(150.dp)
            .width(150.dp),
        shape = ButtonDefaults.shape(
            shape = RoundedCornerShape(0.dp)
        ),
        colors = ButtonDefaults.colors(
            focusedContainerColor = Color.Blue,
            focusedContentColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tabla del", fontSize = 14.sp)
            Text(text, fontSize = 60.sp)
        }
    }
}

@Preview
@Composable
fun ButtonTablePreview() {
    ButtonTable("1", onClick = {})
}

//@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun MultiplyApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Text(
            text = "Elige que tabla quieres practicar",
            color = Color.White
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(35.dp),
            modifier = Modifier.padding(50.dp)
        ) {
            for (i in 1.rangeTo(12)) {
                item {
                    ButtonTable(i.toString(), onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = getRandomExercises(i)
                            println(result)
                        }
                    })
                }
            }
        }
    }
}