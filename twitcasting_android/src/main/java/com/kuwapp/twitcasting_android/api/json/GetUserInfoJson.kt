package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class GetUserInfoJson(@SerializedName("user") val user: UserJson,
                           @SerializedName("supporter_count") val supporterCount: Int,
                           @SerializedName("supporting_count") val supportingCount: Int)