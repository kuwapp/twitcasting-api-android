package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson

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