package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class SearchUsersJson(@SerializedName("users") val users: List<UserJson>)