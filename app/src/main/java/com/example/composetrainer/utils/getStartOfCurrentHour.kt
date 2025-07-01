package com.example.composetrainer.utils

import java.util.Calendar

fun getStartOfCurrentHour(): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}
