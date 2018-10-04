package com.kuwapp.twitcasting_android

object TwitCasting {

    private var clientId: String? = null
    private var clientSecret: String? = null

    fun initialize(clientId: String, clientSecret: String) {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }

    @Throws(TwitCastingNotInitializedException::class)
    internal fun config(): TwitCastingConfig {
        val clientId = clientId ?: throw TwitCastingNotInitializedException()
        val clientSecret = clientSecret ?: throw TwitCastingNotInitializedException()
        return TwitCastingConfig(clientId, clientSecret)
    }

}