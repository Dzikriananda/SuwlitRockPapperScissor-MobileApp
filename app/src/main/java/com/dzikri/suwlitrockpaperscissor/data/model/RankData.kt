package com.dzikri.suwlitrockpaperscissor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RankData(
    val id: String,
    val username: String,
    val score: Int,
    val rank: Int
): Parcelable
