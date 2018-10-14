package com.kuwapp.twitcasting_android.core

import com.kuwapp.twitcasting_android.core.exception.TwitCastingNotInitializedException

object TwitCasting {

    private var clientId: String? = null
    private var clientSecret: String? = null

    fun initialize(clientId: String, clientSecret: String) {
        TwitCasting.clientId = clientId
        TwitCasting.clientSecret = clientSecret
    }

    @Throws(TwitCastingNotInitializedException::class)
    internal fun config(): TwitCastingConfig {
        val clientId = clientId
                ?: throw TwitCastingNotInitializedException()
        val clientSecret = clientSecret
                ?: throw TwitCastingNotInitializedException()
        return TwitCastingConfig(clientId, clientSecret)
    }

}