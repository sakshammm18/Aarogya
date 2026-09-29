package com.example.aarogya.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.aarogya.R

class MainViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(600)
            _isLoading.value = false
        }
    }
}

@Composable
fun SplashScreen(navController: NavController, mainViewModel: MainViewModel = viewModel()) {
    val isLoading by mainViewModel.isLoading.collectAsState()

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

//    val backgroundColor  Color(0xFF4CAF50)
    val backgroundColor = Color(0xFF4CE116)
    val dumbbell = ImageBitmap.imageResource(id = R.drawable.img)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val spacing = 320f
            val imageSize = 80f
            val rotation = 35f

            val cols = (size.width / spacing).toInt() + 2
            val rows = (size.height / spacing).toInt() + 2

            val verticalOffset = -spacing * 2

            for (x in 0 until cols) {
                for (y in 0 until rows) {
                    val offsetX = x * spacing + if (y % 2 == 0) spacing / 2 else 0f
                    val offsetY = y * spacing + verticalOffset

                    withTransform({
                        translate(left = offsetX, top = offsetY)
                        rotate(degrees = rotation, pivot = Offset.Zero)
                        scale(0.4f)
                    }) {
                        drawImage(
                            image = dumbbell,
                            topLeft = Offset.Zero,
                            alpha = 0.15f,
                            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Dumbbells",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(45.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Aarogya",
                fontSize = 44.sp,
                fontWeight = FontWeight.W800,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(navController = rememberNavController(), mainViewModel = MainViewModel())
}