package com.example.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Clip
import com.example.di.ManualDI
import com.example.repository.EditorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ManualDI.getRepository(application)

    private val _clips = MutableStateFlow<List<Clip>>(emptyList())
    val clips: StateFlow<List<Clip>> = _clips.asStateFlow()

    private val _currentProjectId = MutableStateFlow<Long?>(null)

    fun loadProject(projectId: Long) {
        _currentProjectId.value = projectId
        viewModelScope.launch {
            repository.getClipsForProject(projectId).collect {
                _clips.value = it
            }
        }
    }

    fun addClip(clip: Clip) {
        viewModelScope.launch {
            val current = _clips.value.toMutableList()
            current.add(clip)
            _clips.value = current
            repository.saveClips(current)
        }
    }

    fun removeClip(clip: Clip) {
        viewModelScope.launch {
            val current = _clips.value.filter { it.id != clip.id }
            _clips.value = current
            repository.saveClips(current)
        }
    }
}
