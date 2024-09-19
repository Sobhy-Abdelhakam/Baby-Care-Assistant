package dev.sobhy.babycareassistant.vaccination.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaccination(
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val date: String = "",
    val day: String = "",
    val ageGroup: String = "",
    val reason: String = "",
    val notes: String = "",
    val done: Boolean = false
): Parcelable
