package com.kuwapp.twitcasting_android.api.json

import java.util.*

data class GetLiveThumbnailImage(val byteArray: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GetLiveThumbnailImage

        if (!Arrays.equals(byteArray, other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(byteArray)
    }
}