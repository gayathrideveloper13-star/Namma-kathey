package com.mindmatrix.nammakathey.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.nammakathey.Hero
import com.mindmatrix.nammakathey.ui.components.HeroPopupModal
import com.mindmatrix.nammakathey.ui.theme.DarkGreen

// Predefined approximate relative coordinates for the 31 districts of Karnataka to form a stylized map
val districtCoordinates = mapOf(
    "Bidar" to Pair(20f, -320f),
    "Kalaburagi" to Pair(-10f, -260f),
    "Yadgir" to Pair(30f, -210f),
    "Vijayapura" to Pair(-60f, -210f),
    "Bagalkot" to Pair(-50f, -150f),
    "Raichur" to Pair(80f, -160f),
    "Belagavi" to Pair(-120f, -140f),
    "Koppal" to Pair(20f, -90f),
    "Gadag" to Pair(-40f, -90f),
    "Dharwad" to Pair(-90f, -80f),
    "Uttara Kannada" to Pair(-140f, -30f),
    "Haveri" to Pair(-50f, -30f),
    "Ballari" to Pair(90f, -70f),
    "Vijayanagara" to Pair(30f, -30f),
    "Davanagere" to Pair(0f, 20f),
    "Chitradurga" to Pair(60f, 30f),
    "Shivamogga" to Pair(-70f, 50f),
    "Udupi" to Pair(-150f, 80f),
    "Chikkamagaluru" to Pair(-60f, 110f),
    "Tumakuru" to Pair(60f, 110f),
    "Dakshina Kannada" to Pair(-130f, 150f),
    "Hassan" to Pair(-40f, 160f),
    "Mandya" to Pair(40f, 190f),
    "Ramanagara" to Pair(90f, 180f),
    "Bengaluru Rural" to Pair(110f, 120f),
    "Bengaluru Urban" to Pair(130f, 160f),
    "Chikkaballapur" to Pair(130f, 80f),
    "Kolar" to Pair(170f, 120f),
    "Kodagu" to Pair(-80f, 210f),
    "Mysuru" to Pair(-10f, 230f),
    "Chamarajanagar" to Pair(30f, 290f)
)

val districtColors = listOf(
    Color(0xFFFF9933), Color(0xFF138808), Color(0xFF2196F3),
    Color(0xFFFF5252), Color(0xFF9C27B0), Color(0xFFFFC107)
)

@Composable
fun MapScreen(
    heroes: List<Hero>,
    onNavigateToStory: (Hero) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedHero by remember { mutableStateOf<Hero?>(null) }
    var isPopupVisible by remember { mutableStateOf(false) }

    // Map Pan and Zoom State
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))
                )
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 3f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        // Decorative map background elements
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            // Title and Profile Button (Fixed to Top)
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(48.dp)) // To balance the icon on the right
                Text(
                    text = "Namma Karnataka",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreen,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                )
                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = DarkGreen
                    )
                }
            }

            // Zoomable Map Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Plot all 31 districts
                heroes.forEach { hero ->
                    val coords = districtCoordinates[hero.district] ?: Pair(0f, 0f)
                    
                    DistrictMarker(
                        name = hero.district,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = coords.first.dp, y = coords.second.dp),
                        onClick = {
                            selectedHero = hero
                            isPopupVisible = true
                        }
                    )
                }
            }
        }

        // Popup Modal
        selectedHero?.let { hero ->
            HeroPopupModal(
                hero = hero,
                isVisible = isPopupVisible,
                onDismiss = { isPopupVisible = false },
                onViewStory = {
                    isPopupVisible = false
                    onNavigateToStory(hero)
                }
            )
        }
    }
}

@Composable
fun DistrictMarker(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = DarkGreen,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}
