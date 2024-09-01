package dev.sobhy.babycareassistant.diapers.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diapers(
    val id: String = "",
    val date: String = "",
    val day: String = "",
    val numberOfDiapersChange: Int = 0,
    val timesOfDiapersChange: List<String> = emptyList(),
) : Parcelable
