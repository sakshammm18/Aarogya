@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.aarogya.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aarogya.R

@Composable
fun LeaderboardScreen() {
    var selectedTab by remember { mutableStateOf("Friends") }

    val tabs = listOf("Friends", "Global", "Regional")

    // ✅ Sample data
    val players = listOf(
        Player("Benjamin", R.drawable.benjamin, 1, "Top 10%", R.drawable.platinum_badge),
        Player("Christopher", R.drawable.chris, 2, "Top 33.3%", R.drawable.gold_badge),
        Player("Elize", R.drawable.elize, 3, "Top 50%", R.drawable.silver_badge),
        Player("Sophia", R.drawable.chris, 4, "Top 60%", R.drawable.gold_badge),
        Player("Daniel", R.drawable.benjamin, 5, "Top 70%", R.drawable.silver_badge),
        Player("Olivia", R.drawable.elize, 6, "Top 80%", R.drawable.platinum_badge)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 🔹 Title
        Text(
            text = "Leaderboard",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )

        // 🔹 Tabs
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            tabs.forEach { tab ->
                val selected = tab == selectedTab
                TextButton(
                    onClick = { selectedTab = tab },
                    modifier = Modifier
                        .background(
                            if (selected) Color(0xFF4CE116).copy(alpha = 0.2f)
                            else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (selected) Color(0xFF4CE116) else Color.LightGray,
                            shape = RoundedCornerShape(20.dp)

                        )
                        .height(40.dp)
                        .width(100.dp)
                ) {
                    Text(
                        text = tab,
                        color = if (selected) Color(0xFF4CE116) else Color.DarkGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 🔹 Top 3 Players Podium
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            TopPlayer(players[1], Color(0xFFFFC947), 90.dp)   // #2
            TopPlayer(players[0], Color(0xFF4CE116), 120.dp)  // #1
            TopPlayer(players[2], Color(0xFF76E0A3), 80.dp)   // #3
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Remaining Players
        LazyColumn {
            itemsIndexed(players.drop(3)) { _, player ->
                PlayerRow(player)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TopPlayer(player: Player, borderColor: Color, size: Dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(3.dp, borderColor, CircleShape)
        ) {
            Image(
                painter = painterResource(id = player.image),
                contentDescription = null,
                modifier = Modifier.clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(player.name, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun PlayerRow(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "#${player.rank}",
            color = Color.Black,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(id = player.image),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(player.name, color = Color.Black, fontWeight = FontWeight.Medium)
            Text(player.stats, color = Color.Gray, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = player.badge),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}

data class Player(
    val name: String,
    val image: Int,
    val rank: Int,
    val stats: String,
    val badge: Int
)
