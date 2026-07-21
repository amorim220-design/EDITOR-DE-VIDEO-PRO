package com.example.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.data.Clip
import com.example.data.ClipType

@OptIn(UnstableApi::class)
@Composable
fun EditorScreen(
    projectId: Long,
    viewModel: EditorViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val clips by viewModel.clips.collectAsState()
    
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = false
        }
    }

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    LaunchedEffect(clips) {
        exoPlayer.setMediaItems(clips.map { MediaItem.fromUri(it.filePath) })
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Surface(tonalElevation = 8.dp, color = MaterialTheme.colorScheme.background) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Editor Profissional",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { /* Export */ }) {
                        Icon(Icons.Default.IosShare, contentDescription = "Export")
                    }
                }
            }
        },
        bottomBar = {
            EditorBottomBar(onExport = { /* Export */ })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Preview Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Timeline
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "TIMELINE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        MediaPickerButton { uri, type ->
                            viewModel.addClip(
                                Clip(
                                    projectId = projectId,
                                    filePath = uri.toString(),
                                    type = type,
                                    startInMediaMs = 0,
                                    endInMediaMs = 10000,
                                    startInTimelineMs = clips.sumOf { it.durationMs },
                                    durationMs = 10000
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(clips) { clip ->
                            ClipItem(clip)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClipItem(clip: Clip) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                when(clip.type) {
                    ClipType.VIDEO -> Icons.Default.Videocam
                    ClipType.IMAGE -> Icons.Default.Image
                    ClipType.AUDIO -> Icons.Default.MusicNote
                    else -> Icons.Default.TextFields
                },
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                clip.type.name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EditorBottomBar(onExport: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EditorActionButton(Icons.Default.ContentCut, "Cortar")
            EditorActionButton(Icons.Default.Layers, "Camadas")
            EditorActionButton(Icons.Default.AutoFixHigh, "Filtros")
            EditorActionButton(Icons.Default.TextFormat, "Texto")
        }
    }
}

@Composable
fun EditorActionButton(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
