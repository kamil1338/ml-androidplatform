package com.github.kamil1338.recording_app

import com.github.kamil1338.recording_app.dagger.DaggerRecordingAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class ActivityRecognitionApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerRecordingAppComponent.factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
    }

    private fun initLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        }
    }

}