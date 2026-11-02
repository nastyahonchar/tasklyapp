package com.example.taskly

import android.app.Application
import com.example.taskly.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class TasklyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TasklyApplication)
            androidLogger()
        }
    }
}