package com.example.ui.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.Clip
import com.example.data.ClipType

@Composable
fun MediaPickerButton(
    onMediaSelected: (Uri, ClipType) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                // Grant persistable permission for background processing
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                
                val type = when {
                    context.contentResolver.getType(it)?.startsWith("video") == true -> ClipType.VIDEO
                    context.contentResolver.getType(it)?.startsWith("image") == true -> ClipType.IMAGE
                    context.contentResolver.getType(it)?.startsWith("audio") == true -> ClipType.AUDIO
                    else -> ClipType.TEXT
                }
                onMediaSelected(it, type)
            }
        }
    )

    Button(onClick = { launcher.launch(arrayOf("video/*", "image/*", "audio/*")) }) {
        Text("Importar Mídia")
    }
}
