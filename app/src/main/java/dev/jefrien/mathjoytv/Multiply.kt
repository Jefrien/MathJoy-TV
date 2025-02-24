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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.room.Room
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import dev.jefrien.mathjoytv.db.AppDatabase
import dev.jefrien.mathjoytv.db.entities.ExamEntity
import dev.jefrien.mathjoytv.models.MultiplicationRequest
import dev.jefrien.mathjoytv.models.ApiResponse
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import dev.jefrien.mathjoytv.ui.theme.TealDark
import dev.jefrien.mathjoytv.ui.theme.TealLight
import dev.jefrien.mathjoytv.utils.NetworkUtils.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private val apiKey = BuildConfig.api_key;

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

suspend fun getRandomExercises(table: Int, history: List<ExamEntity>): ApiResponse {

    val historyList: MutableList<String> = mutableListOf()
    for(h in history) {
        val itemResponses: List<String> = Json.decodeFromString(h.responses.toString())
        for(item in itemResponses) {
            historyList.add(item)
        }
    }

    val responses = historyList.joinToString(separator = ", ")

    val response: HttpResponse =
        httpClient.post(urlString = "https://jefrien-bot.jefrienalvizures.workers.dev/") {
            contentType(ContentType.Application.Json)

            Log.e("API REQUEST", "Responses: $responses")

            setBody(
                MultiplicationRequest(
                    action = "generateMultiplications",
                    difficulty = "faciles de 2 cifras",
                    quantity = 10,
                    table = table.toString(),
                    history = responses,
                    password = apiKey
                )
            )
        }

    val result: ApiResponse = response.body()
    return result
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonTable(text: String = "Button", onClick: () -> Unit) {
    Button(
        onClick,
        modifier = Modifier
            .height(150.dp)
            .width(150.dp),
        shape = ButtonDefaults.shape(
            shape = RoundedCornerShape(0.dp)
        ),
        colors = ButtonDefaults.colors(
            containerColor = TealLight,
            contentColor = Color.Black,
            focusedContainerColor = TealDark,
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




@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    Dialog (
        onDismissRequest = onDismiss,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
                .background(TealLight, shape = RoundedCornerShape(8.dp))
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {
                Text("Creando tus ejercicios, espera por favor",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
@Preview
fun DialogPreview() {
    LoadingDialog({})
}



//@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun MultiplyApp() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "mathjoy_db"
    ).build()



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
                        showDialog = true
                        CoroutineScope(Dispatchers.IO).launch {

                            val examDao = db.examDao()
                            val history = examDao.loadLastThree()

                            val result = getRandomExercises(i, history)
                            showDialog = false
                            val intent = Intent(context, Exam::class.java)
                            intent.putExtra("exercises", Json.encodeToString(result.data))
                            intent.putExtra("title", "de la tabla del $i")
                            context.startActivity(intent)
                        }
                    })
                }
            }
        }

        if (showDialog) {
            LoadingDialog({ showDialog = false })
        }
    }
}