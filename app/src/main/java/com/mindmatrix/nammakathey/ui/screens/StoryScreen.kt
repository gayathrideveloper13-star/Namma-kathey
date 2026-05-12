package com.mindmatrix.nammakathey.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.nammakathey.Hero
import com.mindmatrix.nammakathey.ui.theme.Saffron
import com.mindmatrix.nammakathey.ui.theme.SoftYellow
import com.mindmatrix.nammakathey.data.AppDatabase
import com.mindmatrix.nammakathey.data.SessionManager
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoryScreen(
    hero: Hero,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val sessionManager = remember { SessionManager(context) }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(hero.id) {
        val userId = sessionManager.getLoggedInUserId()
        if (userId != -1L) {
            db.userDao().incrementStoriesRead(userId)
        }
    }

    DisposableEffect(context) {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.language = Locale.ENGLISH
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance?.stop()
            ttsInstance?.shutdown()
        }
    }

    // Combine all pages into one single long story
    val fullStory = hero.story_en.joinToString(separator = "\n\n")

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Story of ${hero.name_en}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Saffron,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (hero.story_en.isNotEmpty()) {
                        tts?.speak(hero.story_en[0], TextToSpeech.QUEUE_FLUSH, null, null)
                        for (i in 1 until hero.story_en.size) {
                            tts?.speak(hero.story_en[i], TextToSpeech.QUEUE_ADD, null, null)
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play Text To Speech")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(SoftYellow)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image Header
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
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Story Content (Single Scrollable Page)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = fullStory,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        lineHeight = 32.sp,
                        textAlign = TextAlign.Justify,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
