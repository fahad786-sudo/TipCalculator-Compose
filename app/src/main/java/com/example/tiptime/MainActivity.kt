package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {

    var amountInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(15f) }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent.toDouble(), roundUp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A),
                        Color(0xFF9333EA)
                    )
                )
            )
    ) {

        Card(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(24.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "💸 Tip Calculator",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bill Input
                TextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Bill Amount") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tip Percentage Slider
                Text(
                    text = "Tip Percentage: ${tipPercent.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge
                )

                Slider(
                    value = tipPercent,
                    onValueChange = { tipPercent = it },
                    valueRange = 0f..30f
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Round Up Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Round Up Tip")
                    Switch(
                        checked = roundUp,
                        onCheckedChange = { roundUp = it }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Animated Tip Display
                AnimatedContent(
                    targetState = tip,
                    label = ""
                ) { targetTip ->

                    Text(
                        text = targetTip,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Calculates and formats tip
 */
private fun calculateTip(
    amount: Double,
    tipPercent: Double,
    roundUp: Boolean
): String {

    var tip = tipPercent / 100 * amount

    if (roundUp) {
        tip = ceil(tip)
    }

    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun PreviewTipTime() {
    TipTimeTheme {
        TipTimeLayout()
    }
}