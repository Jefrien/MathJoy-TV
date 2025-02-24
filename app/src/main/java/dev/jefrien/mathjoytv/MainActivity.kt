package dev.jefrien.mathjoytv

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Surface
import dev.jefrien.mathjoytv.ui.theme.MathJoyTVTheme
import dev.jefrien.mathjoytv.ui.theme.TealDark
import dev.jefrien.mathjoytv.ui.theme.TealLight

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathJoyTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), shape = RectangleShape
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null,
        modifier = Modifier
            .width(400.dp)
            .padding(10.dp)
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonType(text: String = "Button", iconId: Int = R.drawable.plus, onClick: () -> Unit) {
    Button(
        onClick, modifier = Modifier
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
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = text,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .padding(end = 10.dp, start = 10.dp),
                tint = LocalContentColor.current
            )
            Text(text)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun App() {
    val context = LocalContext.current
    Button (
        onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        },
        modifier = Modifier.padding(16.dp),
        colors = ButtonDefaults.colors(
            contentColor = Color.White,
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            Icons.Default.Settings,
            contentDescription = "Ajustes de la aplicación",
            tint = LocalContentColor.current
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Text(text = "Selecciona el tipo de operación",
            color = Color.White)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(35.dp),
            modifier = Modifier.padding(50.dp)
        ) {
            item {
                ButtonType("Suma", R.drawable.plus, onClick = {
                    /* TODO: Go to suma */
                })
            }
            item {
                ButtonType("Resta", R.drawable.minus, onClick = {
                    /* TODO: Go to resta */
                })
            }
            item {
                ButtonType("Multiplicación", R.drawable.multiply, onClick = {
                    context.startActivity(Intent(context, Multiply::class.java))
                })
            }
            item {
                ButtonType("División", R.drawable.divide, onClick = {
                    /* TODO: Go to división */
                })
            }
        }
    }
}