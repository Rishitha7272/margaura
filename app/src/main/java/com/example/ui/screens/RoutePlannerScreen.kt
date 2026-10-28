package com.example.ui.screens

import androidx.compose.foundation.clickable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun RoutePlannerScreen(onBack: () -> Unit = {}) {
    Scaffold(
        containerColor = OffWhiteBg,
        bottomBar = { OfflineBanner() },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isCompact = maxWidth < 600.dp
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                // Top Input Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .systemBarsPadding()
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HeaderWithBack(onBack)
                        LocationInputFields(
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                
                // Content Below Inputs (Map Preview + List)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        RouteMapPreview(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                    
                    item {
                        Column(
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Option 1: Fastest (Recommended)
                            RouteCard(
                                label = "Fastest",
                                time = "24 min",
                                distance = "8.4 km",
                                trafficCondition = "Moderate",
                                trafficColor = TrafficModerate,
                                hazardCount = 3,
                                routeColor = RouteBlue,
                                isRecommended = true,
                                downloadState = DownloadState.None
                            )
                            
                            // Option 2: Safest (Mid-download)
                            RouteCard(
                                label = "Safest (fewest hazards)",
                                time = "28 min",
                                distance = "9.1 km",
                                trafficCondition = "Light",
                                trafficColor = TrafficLight,
                                hazardCount = 0,
                                routeColor = Amber,
                                isRecommended = false,
                                downloadState = DownloadState.Downloading(62, "48 MB")
                            )
                            
                            // Option 3: Least Traffic
                            RouteCard(
                                label = "Least Traffic",
                                time = "26 min",
                                distance = "9.5 km",
                                trafficCondition = "Light",
                                trafficColor = TrafficLight,
                                hazardCount = 2,
                                routeColor = RouteGreen,
                                isRecommended = false,
                                downloadState = DownloadState.None
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderWithBack(onBack: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Navy,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBack() }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Plan Route",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )
    }
}

@Composable
fun LocationInputFields(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Vertical timeline dots
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp, start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(UserBlue.copy(alpha = 0.2f), CircleShape)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UserBlue, CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(30.dp)
                    .background(SoftGreyBorder)
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = HazardRed,
                modifier = Modifier.size(16.dp)
            )
        }
        
        // Input Fields
        Column(modifier = Modifier.weight(1f)) {
            // From Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(OffWhiteBg, RoundedCornerShape(12.dp))
                    .border(1.dp, SoftGreyBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Current location",
                    color = Navy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // To Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, SoftGreyBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Enter destination",
                    color = SlateText,
                    fontSize = 15.sp
                )
            }
        }
        
        // Actions
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 12.dp)
        ) {
            IconButton(onClick = { }, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap locations",
                    tint = Navy
                )
            }
            IconButton(onClick = { }, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add stop",
                    tint = Navy
                )
            }
        }
    }
}

@Composable
fun RouteMapPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(LightGreyMap)
            .border(width = 1.dp, color = SoftGreyBorder, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val start = Offset(size.width * 0.2f, size.height * 0.8f)
            val end = Offset(size.width * 0.7f, size.height * 0.2f)
            val midX = size.width * 0.45f
            
            // Base Roads
            drawLine(
                color = Color.White,
                start = start.copy(x = start.x - 20f, y = size.height),
                end = end.copy(x = end.x + 20f, y = -20f),
                strokeWidth = 24.dp.toPx()
            )
            
            // Route 3 (Least Traffic - Green) - Wide loop
            val pathGreen = Path().apply {
                moveTo(start.x, start.y)
                quadraticBezierTo(size.width * 0.8f, size.height * 0.7f, end.x, end.y)
            }
            drawPath(pathGreen, RouteGreen.copy(alpha = 0.8f), style = Stroke(6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

            // Route 2 (Safest - Amber) - Slight detour
            val pathAmber = Path().apply {
                moveTo(start.x, start.y)
                quadraticBezierTo(size.width * 0.2f, size.height * 0.3f, end.x, end.y)
            }
            drawPath(pathAmber, Amber.copy(alpha = 0.8f), style = Stroke(6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            
            // Route 1 (Fastest - Blue) - Direct
            val pathBlue = Path().apply {
                moveTo(start.x, start.y)
                quadraticBezierTo(midX, size.height * 0.5f, end.x, end.y)
            }
            drawPath(pathBlue, RouteBlue, style = Stroke(8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            
            // Markers
            // Start
            drawCircle(color = Color.White, radius = 8.dp.toPx(), center = start)
            drawCircle(color = UserBlue, radius = 5.dp.toPx(), center = start)
            // End
            drawCircle(color = Color.White, radius = 10.dp.toPx(), center = end)
            drawCircle(color = HazardRed, radius = 6.dp.toPx(), center = end)
        }
    }
}

sealed class DownloadState {
    object None : DownloadState()
    data class Downloading(val progress: Int, val size: String) : DownloadState()
}

@Composable
fun RouteCard(
    label: String,
    time: String,
    distance: String,
    trafficCondition: String,
    trafficColor: Color,
    hazardCount: Int,
    routeColor: Color,
    isRecommended: Boolean,
    downloadState: DownloadState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(if (isRecommended) 2.dp else 1.dp, if (isRecommended) routeColor else SoftGreyBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(routeColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
            }
            if (isRecommended) {
                Box(
                    modifier = Modifier
                        .background(routeColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "RECOMMENDED",
                        color = routeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Time & Distance
        Text(
            text = "$time · $distance",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = Navy
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Info Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Traffic Chip
            Row(
                modifier = Modifier
                    .background(OffWhiteBg, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(trafficColor, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$trafficCondition Traffic",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy
                )
            }
            
            // Hazard Chip
            Row(
                modifier = Modifier
                    .background(LightAmberBg, RoundedCornerShape(8.dp))
                    .border(1.dp, Amber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = null,
                    tint = Amber,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (hazardCount == 0) "No reported hazards" else "$hazardCount hazards on route",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Amber
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Footer (Buttons or Download Progress)
        when (downloadState) {
            is DownloadState.None -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Start Drive
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Start Drive", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    
                    // Download Outline
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Download",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
            is DownloadState.Downloading -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Downloading offline map & route...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Navy
                        )
                        Text(
                            text = "${downloadState.progress}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { downloadState.progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Navy,
                        trackColor = OffWhiteBg,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Estimated size: ${downloadState.size}",
                        fontSize = 11.sp,
                        color = SlateText
                    )
                }
            }
        }
    }
}

@Composable
fun OfflineBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Navy,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.CloudDone,
                    contentDescription = null,
                    tint = Amber,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "3 routes downloaded for offline use",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            Text(
                text = "Manage",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Amber,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
