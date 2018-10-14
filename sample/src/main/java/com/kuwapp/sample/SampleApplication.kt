package com.kuwapp.sample

import android.app.Application
import com.kuwapp.twitcasting_android.core.TwitCasting

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TwitCasting.initialize(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
    }
}