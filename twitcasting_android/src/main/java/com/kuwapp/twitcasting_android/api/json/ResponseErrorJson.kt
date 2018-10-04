package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class ResponseErrorJson(@SerializedName("error") val error: ErrorJson)