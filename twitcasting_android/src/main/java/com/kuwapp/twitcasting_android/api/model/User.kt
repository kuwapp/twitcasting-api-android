package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.UserJson

data class User(val id: String,
                val screenId: String,
                val name: String,
                val thumbnailUrl: String,
                val profileMessage: String,
                val level: Int,
                val lastMovieId: String?,
                val isLive: Boolean)

internal fun UserJson.toUser(): User {
    return User(
            id = id,
            screenId = screenId,
            name = name,
            thumbnailUrl = thumbnailUrl,
            profileMessage = profileMessage,
            level = level,
            lastMovieId = lastMovieId,
            isLive = isLive
    )
}