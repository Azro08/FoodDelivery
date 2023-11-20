package com.example.fooddelivery.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object Constants {
    const val SHARED_PREF_NAME = "user_pref"
    const val USER_KEY = "user_key"
    const val FOOD_ID = "food_id"

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

}