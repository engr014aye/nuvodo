package com.anasini.nuvodo

import android.app.Application
import com.anasini.nuvodo.di.AppContainer

class NuvodoApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
