package pt.iefp.guess_number_marcelo_alexandre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import androidx.compose.runtime.mutableIntStateOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GuessTheNumberGame() }
    }
}

@Composable
fun GuessTheNumberGame() {
    var targetNumber by remember { mutableIntStateOf(Random.nextInt(1, 21)) }
    var userGuess by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var guessCount by remember { mutableIntStateOf(0) }
    var gameWon by remember { mutableStateOf(false) }
    val rankings = remember { mutableStateListOf<Int>() }

    fun resetGame() {
        targetNumber = Random.nextInt(1, 21)
        userGuess = ""
        feedback = ""
        guessCount = 0
        gameWon = false
    }

    fun checkGuess() {
        val guess = userGuess.toIntOrNull()
        if (guess == null) {
            feedback = "Please enter a valid number"
            return
        }

        guessCount++

        feedback = when {
            guess < targetNumber -> "Too low!"
            guess > targetNumber -> "Too high!"
            else -> {
                gameWon = true
                rankings.add(guessCount)
                rankings.sort()
                "Correct! You guessed it in $guessCount tries"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Guess a number between 1 and 20", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = userGuess,
            onValueChange = { userGuess = it },
            label = { Text("Your guess") },
            singleLine = true,
            enabled = !gameWon
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { checkGuess() }, enabled = !gameWon) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(feedback)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { resetGame() }) {
            Text("Reset Game")
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (rankings.isNotEmpty()) {
            Text("🏆 Leaderboard:", style = MaterialTheme.typography.titleMedium)
            for ((index, score) in rankings.withIndex()) {
                Text("${index + 1}. $score tries")
            }
        }
    }
}
