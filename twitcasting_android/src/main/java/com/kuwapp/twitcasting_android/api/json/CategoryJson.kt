package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class CategoryJson(@SerializedName("id") val id: String,
                        @SerializedName("name") val name: String,
                        @SerializedName("sub_categories") val subCategories: List<SubCategoryJson>)