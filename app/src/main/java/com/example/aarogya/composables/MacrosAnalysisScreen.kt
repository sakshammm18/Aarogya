package com.example.aarogya.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aarogya.data.datastore.UserPreferences
import kotlin.math.roundToInt

@Composable
fun MacrosAnalysisScreen(
    navController: NavHostController,
    age: Int,
    gender: String,
    heightCm: Double,
    weightKg: Double,
    activityLevel: String,
    goal: String
) {
    val userPreferences = UserPreferences(LocalContext.current)

    val backgroundColor = Color(0xFFF8F9FA)
    val accentGreen = Color(0xFF00C853)
    val accentBlue = Color(0xFF2962FF)
    val accentRed = Color(0xFFFF5252)

    // ✅ Default fallback values for safe computation
    val ageVal = if (age > 0) age else 25
    val heightVal = if (heightCm > 0) heightCm else 165.0
    val weightVal = if (weightKg > 0) weightKg else 60.0

    // ✅ Basal Metabolic Rate (Mifflin-St Jeor Equation)
    val s = if (gender.lowercase() == "male") 5 else -161
    val bmr = (10 * weightVal) + (6.25 * heightVal) - (5 * ageVal) + s

    // ✅ Activity Factor Mapping
    val activityFactor = when (activityLevel.lowercase()) {
        "sedentary" -> 1.2
        "light" -> 1.375
        "moderate" -> 1.55
        "active" -> 1.725
        "very active" -> 1.9
        else -> 1.55
    }

    val tdee = bmr * activityFactor

    // ✅ Adjust calories based on goal
    val adjustedCalories = when (goal.lowercase()) {
        "muscle gain" -> tdee + 300
        "fat loss" -> tdee - 300
        else -> tdee
    }

    // ✅ Macronutrient ratio based on goal
    val (proteinRatio, carbRatio, fatRatio) = when (goal.lowercase()) {
        "muscle gain" -> Triple(0.30, 0.50, 0.20)
        "fat loss" -> Triple(0.40, 0.40, 0.20)
        else -> Triple(0.30, 0.45, 0.25)
    }

    val proteinG = (adjustedCalories * proteinRatio / 4).roundToInt()
    val carbsG = (adjustedCalories * carbRatio / 4).roundToInt()
    val fatsG = (adjustedCalories * fatRatio / 9).roundToInt()

    // ✅ Personalized Recommendation
    val recommendation = when (goal.lowercase()) {
        "muscle gain" -> "💪 Increase protein intake and ensure a calorie surplus for muscle recovery."
        "fat loss" -> "🔥 Maintain a small calorie deficit and keep protein intake high."
        else -> "⚖️ Maintain a balanced intake to support your current fitness level."
    }

    // ✅ Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🧩 Header
        Text(
            text = "Your Personalized Nutrition Report",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 👤 User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("👤 User Details", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                NutrientRow("Age", "$ageVal years")
                NutrientRow("Gender", gender)
                NutrientRow("Height", "$heightVal cm")
                NutrientRow("Weight", "$weightVal kg")
                NutrientRow("Activity Level", activityLevel.replaceFirstChar { it.uppercase() })
                NutrientRow("Goal", goal.replaceFirstChar { it.uppercase() })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🍎 Macro Breakdown Donut Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Daily Macro Breakdown", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Canvas(modifier = Modifier.size(180.dp)) {
                    val total = proteinG + carbsG + fatsG
                    val sweepAngles = listOf(
                        (proteinG / total.toFloat()) * 360f,
                        (carbsG / total.toFloat()) * 360f,
                        (fatsG / total.toFloat()) * 360f
                    )
                    val colors = listOf(accentGreen, accentBlue, accentRed)
                    var startAngle = -90f
                    for (i in sweepAngles.indices) {
                        drawArc(
                            color = colors[i],
                            startAngle = startAngle,
                            sweepAngle = sweepAngles[i],
                            useCenter = false,
                            style = Stroke(width = 60f, cap = StrokeCap.Round)
                        )
                        startAngle += sweepAngles[i]
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    LegendItem("Protein", accentGreen)
                    LegendItem("Carbs", accentBlue)
                    LegendItem("Fats", accentRed)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧮 Nutrient Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nutrient Summary", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                NutrientRow("BMR", "${bmr.roundToInt()} kcal")
                NutrientRow("TDEE", "${tdee.roundToInt()} kcal")
                NutrientRow("Calories (Goal)", "${adjustedCalories.roundToInt()} kcal")
                NutrientRow("Protein", "$proteinG g", accentGreen)
                NutrientRow("Carbs", "$carbsG g", accentBlue)
                NutrientRow("Fats", "$fatsG g", accentRed)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 💡 Recommendation Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("💡 Recommendation", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    recommendation,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun NutrientRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = valueColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .padding(end = 4.dp)
                .background(color, RoundedCornerShape(50))
        )
        Text(text, fontSize = 12.sp, color = Color.DarkGray)
    }
}
