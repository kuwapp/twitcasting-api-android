package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class ErrorJson(@SerializedName("code") val code: Int,
                     @SerializedName("message") val message: String)