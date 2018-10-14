package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson
import com.kuwapp.twitcasting_android.api.json.SupportUserJson
import com.kuwapp.twitcasting_android.api.json.SupportingUserJson

data class UserInfo(val user: User,
                    val supporterCount: Int,
                    val supportingCount: Int)

internal fun GetUserInfoJson.toUserInfo(): UserInfo {
    return UserInfo(
            user = user.toUser(),
            supporterCount = supporterCount,
            supportingCount = supportingCount
    )
}

internal fun SupportingUserJson.toUserInfo(): UserInfo {
    return UserInfo(
            user = User(
                    id = id,
                    screenId = screenId,
                    name = name,
                    thumbnailUrl = thumbnailUrl,
                    profileMessage = profileMessage,
                    level = level,
                    lastMovieId = lastMovieId,
                    isLive = isLive
            ),
            supportingCount = supportingCount,
            supporterCount = supporterCount
    )
}

internal fun SupportUserJson.toUserInfo(): UserInfo {
    return UserInfo(
            user = User(
                    id = id,
                    screenId = screenId,
                    name = name,
                    thumbnailUrl = thumbnailUrl,
                    profileMessage = profileMessage,
                    level = level,
                    lastMovieId = lastMovieId,
                    isLive = isLive
            ),
            supportingCount = supportingCount,
            supporterCount = supporterCount
    )
}