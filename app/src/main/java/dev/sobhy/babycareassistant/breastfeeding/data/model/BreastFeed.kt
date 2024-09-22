package dev.sobhy.babycareassistant.breastfeeding.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BreastFeed(
    val id: String = "",
    val date: String = "",
    val day: String = "",
    val numberOfFeedingsPerDay : Int = 0,
    val timeOfTimes: List<FeedingTimes> = emptyList()
) : Parcelable

@Parcelize
data class FeedingTimes(
    val time: String = "",
    val amountOfMilk: Int = 0
): Parcelable
