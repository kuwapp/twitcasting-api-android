package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class GetSupportingUsersJson(@SerializedName("total") val total: Int,
                                  @SerializedName("supporting") val supportingUsers: List<SupportingUserJson>)