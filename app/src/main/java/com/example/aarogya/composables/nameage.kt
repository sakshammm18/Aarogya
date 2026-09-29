package com.example.aarogya.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aarogya.data.datastore.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(onSubmit: (String, String, String, String, String, String, String) -> Unit) {

    val userPreferences = UserPreferences(LocalContext.current)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    var activityIndex by remember { mutableStateOf(2f) } // Default "Moderate"
    var goalIndex by remember { mutableStateOf(0f) } // Default "Muscle Gain"

    val activityLevels = listOf("Sedentary", "Light", "Moderate", "Active", "Very Active")
    val goals = listOf("Muscle Gain", "Fat Loss", "Maintain")

    val accentGreen = Color(0xFF4CE116)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Let's know you better!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextFieldWithStyle("Username", username) { username = it }
            OutlinedTextFieldWithStyle("Age", age) { age = it }
            OutlinedTextFieldWithStyle("Gender", gender) { gender = it }
            OutlinedTextFieldWithStyle("Height (cm)", height) { height = it }
            OutlinedTextFieldWithStyle("Weight (kg)", weight) { weight = it }

            Spacer(modifier = Modifier.height(25.dp))

            // 🔹 Activity Level
            Text("Activity Level: ${activityLevels[activityIndex.toInt()]}", fontWeight = FontWeight.SemiBold)
            Slider(
                value = activityIndex,
                onValueChange = { activityIndex = it },
                valueRange = 0f..4f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = accentGreen,
                    activeTrackColor = accentGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Goal
            Text("Goal: ${goals[goalIndex.toInt()]}", fontWeight = FontWeight.SemiBold)
            Slider(
                value = goalIndex,
                onValueChange = { goalIndex = it },
                valueRange = 0f..2f,
                steps = 1,
                colors = SliderDefaults.colors(
                    thumbColor = accentGreen,
                    activeTrackColor = accentGreen
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    when {
                        username.isBlank() || age.isBlank() || gender.isBlank() ||
                                height.isBlank() || weight.isBlank() -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("All fields are required.")
                            }
                        }
                        age.toIntOrNull() == null || height.toFloatOrNull() == null || weight.toFloatOrNull() == null -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please enter valid numbers for age, height, and weight.")
                            }
                        }
                        else -> {
                            val selectedActivity = activityLevels[activityIndex.toInt()].lowercase()
                            val selectedGoal = goals[goalIndex.toInt()].lowercase().replace(" ", "_")

                            onSubmit(username, age, gender, height, weight, selectedActivity, selectedGoal)

                            scope.launch {
                                userPreferences.saveUserData(
                                    age.toInt(),
                                    gender,
                                    height.toInt(),
                                    weight.toInt(),
                                    username
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun OutlinedTextFieldWithStyle(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.DarkGray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
