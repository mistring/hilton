package com.mstringham.ipgeolocation

import android.app.Application
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// Custom Timber logger class; prints line number and method name, etc.
class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "[C:%s] [M:%s] [L:%s]",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
    }
}

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Add (plant) Timber logger here
        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        if (isDebuggable) {
            Timber.plant(MyDebugTree())
        }
    }
}
