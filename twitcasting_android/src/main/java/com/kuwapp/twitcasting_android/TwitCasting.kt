package com.kuwapp.twitcasting_android

object TwitCasting {

    private var clientId: String? = null
    private var clientSecret: String? = null

    fun initialize(clientId: String, clientSecret: String) {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }

    internal fun config(): TwitCastingConfig {
        val clientId = clientId ?: throw RuntimeException()
        val clientSecret = clientSecret ?: throw RuntimeException()
        return TwitCastingConfig(clientId, clientSecret)
    }

}