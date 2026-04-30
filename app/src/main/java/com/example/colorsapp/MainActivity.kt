package com.example.colorsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    var activeBox by remember { mutableStateOf(-1) }
    var hits by remember { mutableStateOf(0) }
    var misses by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Whack-a-Color!",
            fontSize = 28.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Hits: $hits Misses: $misses",
            fontSize = 24.sp,
            color = Color.White
        )

        Row {
            ColorBox(colors[0]) {
                checkHit(0, activeBox, { hits++; activeBox = -1 }, { misses++ } )
            }
            Spacer(modifier = Modifier.width(10.dp))
            ColorBox(colors[1]) {
                checkHit(1, activeBox,{ hits++; activeBox = -1 }, { misses++ } )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            ColorBox(colors[2]) {
                checkHit(2, activeBox, { hits++; activeBox = -1 }, { misses++ } )
            }
            Spacer(modifier = Modifier.width(10.dp))
            ColorBox(colors[3]) {
                checkHit(3, activeBox, { hits++; activeBox = -1 } ,{ misses++ } )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.size(150.dp, 70.dp),
            onClick = {
                running = !running

                if (running) {
                    hits = 0
                    misses = 0
                    scope.launch {
                        val order = listOf(0, 1, 3, 2)
                        var step = 0

                        while (running) {
                            val newColors = originalColors.toMutableList()
                            val chosenIndex =
                                if (randomize) Random.nextInt(0, 4)
                                else order[step]
                            activeBox = chosenIndex
                            newColors[chosenIndex] = Color.White
                            colors = newColors

                            delay(700)
                            if (activeBox == chosenIndex) {
                                misses++
                            }
                            activeBox = -1
                            colors = originalColors
                            delay(300)
                            step = (step + 1) % 4
                        }
                    }
                } else {
                    activeBox = -1
                    colors = originalColors
                }
            }
        ) {
            Text(
                if (running) "Stop" else "Start",
                fontSize = 22.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = randomize,
                onCheckedChange = { randomize = it }
            )
            Text(
                text = "Randomize?",
                color = Color.Magenta,
                fontSize = 22.sp
            )
        }
    }
}
fun checkHit(
    boxNumber: Int,
    activeBox:Int,
    onHit: () -> Unit,
    onMiss:() -> Unit
){
    if (boxNumber == activeBox){
        onHit()
    } else {
        onMiss()
    }
}

@Composable
fun ColorBox(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .background(color)
            .clickable{ onClick() }
    )
}


