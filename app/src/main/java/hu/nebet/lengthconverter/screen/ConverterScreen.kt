package hu.nebet.lengthconverter.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.nebet.lengthconverter.R
import hu.nebet.lengthconverter.ui.theme.LengthConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen() {
    val context = LocalContext.current

    var inputText by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("0") }
    var errorText by rememberSaveable { mutableStateOf("") }
    var inputErrorState by rememberSaveable { mutableStateOf(false) }

    fun validate(text: String) {
        try {
            val value = text.toFloat()
            errorText = ""
            inputErrorState = false
        } catch (e: NumberFormatException) {
            errorText = context.getString(R.string.invalid_input)
            inputErrorState = true
        }
    }

    fun convertAndSetResult(conversionFactor: Float) {
        try {
            val kmValue = inputText.toFloat()
            result = (kmValue * conversionFactor).toString()
            errorText = ""
        } catch (e: Exception) {
            errorText = "Error: ${e.message}"
        }
    }

    val conversions = listOf(
        Conversion("km -> miles", 0.621f),
        Conversion("km -> yard", 1093.61f),
        Conversion("km -> foot", 3280.83f),
        Conversion("km -> inch", 39370.07f),
        Conversion("km -> meter", 1000f),
        Conversion("km -> cm", 100000f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            label = { Text(text = stringResource(R.string.length_to_be_converted)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            value = inputText,
            onValueChange = {
                inputText = it
                validate(inputText)
            },
            isError = inputErrorState,
            trailingIcon = {
                if (inputErrorState)
                    Icon(
                        Icons.Filled.Warning,
                        "error", tint = MaterialTheme.colorScheme.error
                    )
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until 3) {
                val conversion = conversions[i]
                Button(onClick = { convertAndSetResult(conversion.factor) }) {
                    Text(text = conversion.label)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 3 until 6) {
                val conversion = conversions[i]
                Button(onClick = { convertAndSetResult(conversion.factor) }) {
                    Text(text = conversion.label)
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .height(100.dp)
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = if (errorText.isEmpty()) String.format("%.3f", result.toFloat())
                else errorText,
                fontSize = 26.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

data class Conversion(val label: String, val factor: Float)

@Preview(showBackground = true)
@Composable
fun ConverterPreview() {
    LengthConverterTheme {
        ConverterScreen()
    }
}