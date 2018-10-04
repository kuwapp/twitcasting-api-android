package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.TwitCasting
import com.kuwapp.twitcasting_android.TwitCastingNotInitializedException

object TwitCastingApi {

    private val client by lazy {
        val config = TwitCasting.config()
        createTwitCastingApiClient(config.clientId, config.clientSecret)
    }

    @JvmStatic
    fun client(): TwitCastingApiClient {
        return client
    }

}