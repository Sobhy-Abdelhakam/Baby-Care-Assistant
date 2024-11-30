package dev.sobhy.babycareassistant.alarm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedingSchedule(
    val age: String = "",
    val notes: String = "",
    val daily_feedings: Int = 0,
    val milk_quantity: String = "",
): Parcelable
