package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class GetCategoriesJson(@SerializedName("categories") val categories: List<CategoryJson>)