package com.example.ui.screens

import androidx.compose.foundation.clickable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ARDriveScreen(onClose: () -> Unit = {}) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF94A3B8), // Simulated Sky
                        Color(0xFF475569)  // Simulated Road
                    )
                )
            )
    ) {
        // Simulated Camera Passthrough / AR Road perspective
        Canvas(modifier = Modifier.fillMaxSize()) {
            val horizonY = size.height * 0.4f
            val centerX = size.width / 2f
            
            // Perspective Road lines
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(centerX - 40f, horizonY),
                end = Offset(0f, size.height),
                strokeWidth = 12.dp.toPx()
            )
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(centerX + 40f, horizonY),
                end = Offset(size.width, size.height),
                strokeWidth = 12.dp.toPx()
            )
            
            // AR Hazard Bounding Overlay (Painted on the road)
            val boxY = size.height * 0.65f
            val boxWidth = 350f
            val boxHeight = 120f
            val path = Path().apply {
                moveTo(centerX - boxWidth / 3, boxY)
                lineTo(centerX + boxWidth / 3, boxY)
                lineTo(centerX + boxWidth, boxY + boxHeight)
                lineTo(centerX - boxWidth, boxY + boxHeight)
                close()
            }
            drawPath(
                path = path,
                color = HazardRed.copy(alpha = 0.2f)
            )
            drawPath(
                path = path,
                color = HazardRed,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
            )
        }
        
        // Subtle Low-Opacity "AR" Badge
        Text(
            text = "AR",
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 24.dp)
        )
        
        // Top HUD (Close button)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Close, contentDescription = "Exit AR", tint = Color.White)
            }
        }
        
        // Proximity Alert Card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-60).dp)
                .padding(horizontal = 32.dp)
                .shadow(24.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(2.dp, HazardRed.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(HazardRed.copy(alpha = 0.1f), CircleShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("🕳️", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pothole Ahead",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Slow down • 20m",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HazardRed
            )
        }
        
        // Bottom Navigation HUD
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .shadow(24.dp, RoundedCornerShape(32.dp))
                .background(Color.White, RoundedCornerShape(32.dp))
                .border(1.dp, SoftGreyBorder, RoundedCornerShape(32.dp))
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "NH-48",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SlateText
                )
                Text(
                    text = "Continue Straight",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
            }
            
            // Speed indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "45",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Navy
                )
                Text(
                    text = "km/h",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateText
                )
            }
        }
    }
}
