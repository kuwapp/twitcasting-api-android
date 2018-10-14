package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class GetSupportingStatusJson(@SerializedName("is_supporting") val isSupporting: Boolean,
                                   @SerializedName("target_user") val user: UserJson)