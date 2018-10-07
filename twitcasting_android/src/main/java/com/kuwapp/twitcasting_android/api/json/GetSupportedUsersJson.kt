package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class GetSupportedUsersJson(@SerializedName("total") val total: Int,
                                 @SerializedName("supporters") val supportedUsers: List<SupportUserJson>)