package com.example.colorsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorsapp.ui.theme.ColorsAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorsAppTheme {
                    ColorsApp(
                    )
                }
            }
        }
    }

@Composable
fun ColorsApp() {
    var colors by remember {
        mutableStateOf(listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue))
    }

    val originalColors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue)
    var randomize by remember { mutableStateOf(false) }
    var running by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Colors!",
            fontSize = 28.sp,
            color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            ColorBox(colors[0])
            Spacer(modifier = Modifier.width(10.dp))
            ColorBox(colors[1])
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            ColorBox(colors[2])
            Spacer(modifier = Modifier.width(10.dp))
            ColorBox(colors[3])
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier =Modifier.size(150.dp,70.dp),
            onClick = {
                running = !running

                if (running) {
                    scope.launch {
                        val order = listOf(0, 1, 3, 2)
                        var step = 0

                        while (running) {
                            val newColors = originalColors.toMutableList()
                            val chosenIndex =
                                if (randomize) Random.nextInt(0, 4)
                                else order[step]
                            newColors[chosenIndex] = Color.White
                            colors = newColors

                            delay(400)
                            colors = originalColors
                            delay(400)
                            step = (step + 1) % 4
                        }
                    }
                }
            }
        ){
            Text(if (running) "Stop" else "Start")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = randomize,
                onCheckedChange = { randomize = it }
            )
            Text(
                text="Randomize?",
                color = Color.Magenta,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun ColorBox(color: Color) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .background(color)
    )
}


