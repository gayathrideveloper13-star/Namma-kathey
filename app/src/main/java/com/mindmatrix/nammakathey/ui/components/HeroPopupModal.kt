package com.mindmatrix.nammakathey.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindmatrix.nammakathey.Hero
import com.mindmatrix.nammakathey.ui.theme.Saffron
import com.mindmatrix.nammakathey.ui.theme.SoftYellow

@Composable
fun HeroPopupModal(
    hero: Hero,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onViewStory: () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Dim background
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
                    exit = scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentHeight()
                            .clickable(enabled = false) {}, // Consume clicks
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftYellow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = hero.district,
                                style = MaterialTheme.typography.titleLarge,
                                color = Saffron,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Hero Image Placeholder (uses resource ID or Coil later)
                            val imageResId = when(hero.image_url) {
                                "ic_rayanna" -> com.mindmatrix.nammakathey.R.drawable.hero_rayanna
                                "ic_obavva" -> com.mindmatrix.nammakathey.R.drawable.hero_obavva
                                "ic_kempegowda" -> com.mindmatrix.nammakathey.R.drawable.hero_kempegowda
                                "hero_abbakka" -> com.mindmatrix.nammakathey.R.drawable.hero_abbakka
                                "hero_visvesvaraya" -> com.mindmatrix.nammakathey.R.drawable.hero_visvesvaraya
                                else -> com.mindmatrix.nammakathey.R.drawable.hero_rayanna
                            }

                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = hero.name_en,
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = hero.name_en,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = hero.name_kn,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onViewStory,
                                colors = ButtonDefaults.buttonColors(containerColor = Saffron),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text(
                                    text = "Read Story",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
