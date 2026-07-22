package com.example

import android.app.Application
import android.util.Log
import com.example.di.ManualDI

class ProVideoApplication : Application() {
    companion object {
        private const val TAG = "ProVideoApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application onCreate")
        // Inicializa o ManualDI com o contexto da aplicação
        ManualDI.initialize(this)
    }
}
