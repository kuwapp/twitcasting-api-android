package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class SubCategoryJson(@SerializedName("id") val id: String,
                           @SerializedName("name") val name: String,
                           @SerializedName("count") val count: Int)