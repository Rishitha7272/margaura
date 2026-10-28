package com.example.ui.screens

import androidx.compose.foundation.clickable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HomeScreen(onNavigateToAR: () -> Unit = {}, onNavigateToRoute: () -> Unit = {}) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isCompact = maxWidth < 600.dp
        Scaffold(
            containerColor = LightGreyMap,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            HomeScreenContent(innerPadding, isCompact, onNavigateToAR, onNavigateToRoute)
        }
    }
}

@Composable
fun HomeScreenContent(
    innerPadding: PaddingValues, 
    isCompact: Boolean, 
    onNavigateToAR: () -> Unit, 
    onNavigateToRoute: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Base Map Layer
        InteractiveMapLayer()

        // Foreground UI Overlays
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TopSection(isCompact)
                BottomSection(isCompact, onNavigateToRoute)
            }

            ARToggleButton(
                onClick = onNavigateToAR,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = if (isCompact) 140.dp else 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun InteractiveMapLayer() {
    Box(modifier = Modifier.fillMaxSize().background(LightGreyMap)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // River (thick curvy line)
            drawPath(
                path = Path().apply {
                    moveTo(size.width * 0.8f, -100f)
                    quadraticBezierTo(size.width * 0.5f, size.height * 0.4f, size.width * 1.2f, size.height)
                },
                color = MapRiver,
                style = Stroke(width = 120.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // Main Road
            drawLine(
                color = Color.White,
                start = Offset(size.width * 0.2f, 0f),
                end = Offset(size.width * 0.45f, size.height),
                strokeWidth = 36.dp.toPx()
            )
            
            // Cross Road
            drawLine(
                color = Color.White,
                start = Offset(0f, size.height * 0.4f),
                end = Offset(size.width, size.height * 0.35f),
                strokeWidth = 24.dp.toPx()
            )

            // Cracked/damaged road segment (Dashed red line)
            drawLine(
                color = HazardRed,
                start = Offset(size.width * 0.3f, size.height * 0.4f),
                end = Offset(size.width * 0.38f, size.height * 0.7f),
                strokeWidth = 6.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 20f), 0f)
            )
        }

        // High pedestrian/cyclist activity zone (Soft blue translucent circle)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = 160.dp)
                .size(160.dp)
                .background(ZoneBlue, CircleShape)
        )

        // User Location (Blue dot with direction cone)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = (-20).dp, y = (-240).dp)
        ) {
            // Direction cone
            Canvas(modifier = Modifier.size(80.dp).offset(y = (-30).dp, x = (-28).dp)) {
                val path = Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(size.width * 0.1f, 0f)
                    quadraticBezierTo(size.width / 2f, -15f, size.width * 0.9f, 0f)
                    close()
                }
                drawPath(path, color = UserCone)
            }
            // Blue Dot
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, CircleShape)
                    .border(3.dp, Color.White, CircleShape)
                    .shadow(4.dp, CircleShape)
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .background(UserBlue, CircleShape)
                )
            }
        }

        // Road Construction Popup Card
        HazardPopupCard(
            title = "Road Construction",
            subtitle = "300m ahead — Detour available",
            severityColor = HazardOrange,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-30).dp, y = 190.dp)
        )

        // Pothole (Red circular pin)
        HazardPin(
            color = HazardRed,
            emoji = "🕳️",
            modifier = Modifier.align(Alignment.Center).offset(x = (-20).dp, y = (-80).dp)
        )

        // Faded/Zebra Crossing (Amber striped pin)
        AmberStripedPin(
            modifier = Modifier.align(Alignment.BottomStart).offset(x = 60.dp, y = (-360).dp)
        )

        // Broken/under-construction bridge (Orange pin)
        HazardPin(
            color = HazardOrange,
            emoji = "🚧",
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-80).dp, y = 300.dp)
        )

        // Missing/damaged signboard (Grey pin)
        HazardPin(
            color = HazardGrey,
            emoji = "🪧",
            modifier = Modifier.align(Alignment.CenterStart).offset(x = 100.dp, y = 40.dp)
        )
    }
}

@Composable
fun HazardPopupCard(
    title: String,
    subtitle: String,
    severityColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(240.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, SoftGreyBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(OffWhiteBg, RoundedCornerShape(8.dp))
                    .border(1.dp, SoftGreyBorder, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image, 
                    contentDescription = "Photo", 
                    tint = SlateText, 
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                    // Severity Dot
                    Box(modifier = Modifier.size(8.dp).background(severityColor, CircleShape))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.sp, color = SlateText)
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = SoftGreyBorder)
        Spacer(modifier = Modifier.height(10.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning, 
                contentDescription = null, 
                tint = Navy, 
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Report similar issue", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Navy)
        }
    }
}

@Composable
fun HazardPin(color: Color, emoji: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(6.dp, CircleShape)
            .background(color, CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 16.sp)
    }
}

@Composable
fun AmberStripedPin(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(6.dp, CircleShape)
            .border(2.dp, Color.White, CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Amber)
            val strokeW = 3.dp.toPx()
            for (i in -2..4) {
                drawLine(
                    color = Color.White.copy(alpha = 0.5f),
                    start = Offset(0f, i * 14.dp.toPx()),
                    end = Offset(size.width, (i + 1) * 14.dp.toPx()),
                    strokeWidth = strokeW
                )
            }
        }
        Text("🦓", fontSize = 14.sp)
    }
}

@Composable
fun ARToggleButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, SoftGreyBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Visibility,
            contentDescription = "AR View",
            tint = Navy,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "AR View",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )
    }
}

@Composable
fun TopSection(isCompact: Boolean) {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .padding(16.dp)
    ) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, CircleShape)
                .background(Color.White, CircleShape)
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Navy)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Where to?",
                color = SlateText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Navy, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("JD", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filters
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipSelected("All")
            FilterChipFallback("Potholes")
            FilterChipFallback("Crossings")
            FilterChipFallback("Road damage")
            FilterChipFallback("Bridges")
            FilterChipFallback("Signage")
        }
    }
}

@Composable
fun FilterChipSelected(label: String) {
    Row(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Navy, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Composable
fun FilterChipFallback(label: String) {
    Row(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, SoftGreyBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Navy.copy(alpha = 0.7f))
    }
}

@Composable
fun BottomSection(isCompact: Boolean, onNavigateToRoute: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCompact) Alignment.CenterHorizontally else Alignment.End
    ) {
        // Floating Action Buttons Row (Legend + Plan Route)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Legend Button
            SmallFloatingActionButton(
                onClick = { },
                containerColor = Color.White,
                contentColor = Navy,
                modifier = Modifier.shadow(8.dp, CircleShape),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Layers, contentDescription = "Legend")
            }

            // Plan Route FAB
            ExtendedFloatingActionButton(
                onClick = onNavigateToRoute,
                containerColor = Navy,
                contentColor = Color.White,
                modifier = Modifier.shadow(12.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Plan Route", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Plan Route")
            }
        }

        // Partially Expanded Bottom Sheet Mock
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .shadow(24.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .border(1.dp, SoftGreyBorder, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .background(Color(0xFFE2E8F0), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nearby hazards",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Scrollable List of Hazards
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                HazardListItem(
                    iconEmoji = "🕳️",
                    title = "Pothole",
                    distance = "350m",
                    severityColor = HazardRed
                )
                HazardListItem(
                    iconEmoji = "🦓",
                    title = "Faded Crossing",
                    distance = "500m",
                    severityColor = Amber
                )
                HazardListItem(
                    iconEmoji = "🚧",
                    title = "Broken Bridge",
                    distance = "1.2km",
                    severityColor = HazardOrange
                )
            }
        }
    }
}

@Composable
fun HazardListItem(iconEmoji: String, title: String, distance: String, severityColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(OffWhiteBg, RoundedCornerShape(12.dp))
                .border(1.dp, SoftGreyBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(iconEmoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = distance, fontSize = 13.sp, color = SlateText)
        }
        // Severity Dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(severityColor, CircleShape)
        )
    }
}

@Composable
fun CustomBottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, SoftGreyBorder)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Outlined.Explore,
            label = "Explore",
            isSelected = true
        )
        BottomNavItem(
            icon = Icons.Outlined.Visibility,
            label = "AR View",
            isSelected = false
        )
        BottomNavItem(
            icon = Icons.Outlined.CloudOff,
            label = "Offline",
            isSelected = false
        )
        BottomNavItem(
            icon = Icons.Outlined.Person,
            label = "Profile",
            isSelected = false
        )
    }
}

@Composable
fun CustomNavRail() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color.White)
            .border(1.dp, SoftGreyBorder)
            .padding(vertical = 24.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        BottomNavItem(
            icon = Icons.Outlined.Explore,
            label = "Explore",
            isSelected = true
        )
        BottomNavItem(
            icon = Icons.Outlined.Visibility,
            label = "AR View",
            isSelected = false
        )
        BottomNavItem(
            icon = Icons.Outlined.CloudOff,
            label = "Offline",
            isSelected = false
        )
        BottomNavItem(
            icon = Icons.Outlined.Person,
            label = "Profile",
            isSelected = false
        )
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean
) {
    val alpha = if (isSelected) 1f else 0.4f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) Navy.copy(alpha = 0.05f) else Color.Transparent,
                    CircleShape
                )
                .padding(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Navy.copy(alpha = alpha),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Navy.copy(alpha = alpha)
        )
    }
}
