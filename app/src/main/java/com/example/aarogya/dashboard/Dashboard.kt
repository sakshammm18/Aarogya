@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.aarogya.dashboard

import co.yml.charts.ui.linechart.model.Line
import coil3.compose.AsyncImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.aarogya.R
import kotlinx.coroutines.delay
import java.util.stream.Stream

@Composable
fun WearableDashboard(
    navController: NavController,
    username: String,
    age: Int,
    gender: String,
    height: Float,
    weight: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // ✅ Welcome Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // 🔹 Row for Welcome Text + Profile Image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Welcome, $username!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1EE56E)
                        )
                    )

                    // ✅ Profile Image beside welcome text
                    AsyncImage(
                        model = "https://randomuser.me/api/portraits/women/65.jpg",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 User Info below
                Text("Age: $age years", color = Color.DarkGray, fontSize = 18.sp)
                Text("Gender: $gender", color = Color.DarkGray, fontSize = 18.sp)
                Text("Height: $height cm", color = Color.DarkGray, fontSize = 18.sp)
                Text("Weight: $weight kg", color = Color.DarkGray, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Rest of the Dashboard
        Text(
            text = "Wearable Data",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        MetricsGrid()
        Spacer(modifier = Modifier.height(24.dp))
        StepsTrendChart()
        Spacer(modifier = Modifier.height(24.dp))
        CaloriesBurnedChart()
    }
}


@Composable
fun MetricsGrid() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricCard(
                title = "Steps",
                value = "9,850",
                unit = "steps",
                progress = 9850f / 10000f,
                goal = "Goal: 10,000 steps",
                color = Color(0xFF4CE116)

            )
            MetricCard(
                title = "Calories Burned",
                value = "480",
                unit = "kcal",
                progress = 480f / 600f,
                goal = "Goal: 600 kcal",
                color = Color(0xFF2196F3)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricSmallCard("Avg. Heart Rate", "72", "bpm", Color(0xFFFF6D6D),)
            MetricSmallCard("Distance", "6.5", "km", Color(0xFF673AB7))
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String, progress: Float, goal: String, color: Color) {
    Card(
        modifier = Modifier
//            .background(Color.White)
            .width(170.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier
            .background(Color.White)
            .padding(12.dp)
        ) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$value", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(unit, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(progress = progress, color = color, modifier = Modifier.fillMaxWidth())
            Text(goal, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Start)
        }
    }
}

@Composable
fun MetricSmallCard(title: String, value: String, unit: String, color: Color) {
    Card(
        modifier = Modifier
            .background(Color.White)
            .width(170.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(unit, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun StepsTrendChart() {
    val yPoints = listOf(
        Point(0f, 400f),
        Point(1f, 320f),
        Point(2f, 450f),
        Point(3f, 480f),
        Point(4f, 560f),
        Point(5f, 590f),
        Point(6f, 470f)
    )

    val xPoints = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    var displayedPoints by remember { mutableStateOf(listOf<Point>()) }

    LaunchedEffect(Unit) {
        yPoints.forEachIndexed { index, _ ->
            delay(300)
            displayedPoints = yPoints.take(index + 1)
        }
    }

    if (displayedPoints.isNotEmpty()) {
        val minY = yPoints.minOf { it.y }
        val maxY = yPoints.maxOf { it.y }
        val stepSizeY = ((maxY - minY) / 4).toInt()

        val data = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = displayedPoints,
                        lineStyle = LineStyle(color = Color(0xFF2196F3)),
                        IntersectionPoint(Color(0xFF2196F3)),
                        SelectionHighlightPoint(Color(0xFF1976D2)),
                        shadowUnderLine = ShadowUnderLine(Color(0x332196F3))
                    )
                )
            ),
            xAxisData = AxisData.Builder()
                .steps(xPoints.size - 1)
                .axisStepSize(50.dp)
                .axisLabelColor(Color.Black)
                .axisLineColor(Color.Gray)
                .labelAndAxisLinePadding(10.dp)
                .labelData { i -> xPoints[i] }
                .backgroundColor(Color.White)
                .build(),
            yAxisData = AxisData.Builder()
                .steps(4)
                .axisLabelColor(Color.Black)
                .axisLineColor(Color.Gray)
                .labelData { i -> "${(minY + (i * stepSizeY)).toInt()} steps" }
                .labelAndAxisLinePadding(10.dp)
                .backgroundColor(Color.White)
                .build(),
            gridLines = GridLines(),
            backgroundColor = Color.White
        )

        ChartCard(
            title = "Daily Steps Trend",
            subtitle = "You've maintained a strong step count this week, exceeding your goal on most days."
        ) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .align(Alignment.Center),
                lineChartData = data
            )
        }
    } else {
        ChartCard(title = "Daily Steps Trend", subtitle = "Loading data...") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun CaloriesBurnedChart() {
    val caloriePoints = listOf(
        Point(0f, 400f),
        Point(1f, 320f),
        Point(2f, 450f),
        Point(3f, 480f),
        Point(4f, 560f),
        Point(5f, 590f),
        Point(6f, 470f)
    )

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    var displayedPoints by remember { mutableStateOf(listOf<Point>()) }

    LaunchedEffect(Unit) {
        caloriePoints.forEachIndexed { index, _ ->
            delay(300)
            displayedPoints = caloriePoints.take(index + 1)
        }
    }

    if (displayedPoints.isNotEmpty()) {
        val minCalories = caloriePoints.minOf { it.y }
        val maxCalories = caloriePoints.maxOf { it.y }
        val stepSize = ((maxCalories - minCalories) / 4).toInt()

        val data = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = displayedPoints,
                        lineStyle = LineStyle(color = Color(0xFFFF9800)),
                        IntersectionPoint(Color(0xFFFFA726)),
                        SelectionHighlightPoint(Color(0xFFFFA726)),
                        shadowUnderLine = ShadowUnderLine(Color(0x33FF9800))
                    )
                )
            ),
            xAxisData = AxisData.Builder()
                .steps(days.size - 1)
                .axisStepSize(50.dp)
                .axisLabelColor(Color.Black)
                .axisLineColor(Color.Gray)
                .labelAndAxisLinePadding(10.dp)
                .labelData { i -> days[i] }
                .backgroundColor(Color.White)
                .build(),
            yAxisData = AxisData.Builder()
                .steps(4)
                .axisLabelColor(Color.Black)
                .axisLineColor(Color.Gray)
                .labelData { i -> "${(minCalories + (i * stepSize)).toInt()} kcal" }
                .labelAndAxisLinePadding(10.dp)
                .backgroundColor(Color.White)
                .build(),
            gridLines = GridLines(),
            backgroundColor = Color.White
        )

        ChartCard(
            title = "Calories Burned Overview",
            subtitle = "Your calorie burn has been consistent, showing good effort in your workouts."
        ) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .align(Alignment.Center),
                lineChartData = data
            )
        }
    } else {
        ChartCard(title = "Calories Burned Overview", subtitle = "Loading data...") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ChartCard(title: String, subtitle: String, content: @Composable BoxScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                content()

                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .width(20.dp)
                        .background(Color.White)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-20).dp)
                        .zIndex(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}


