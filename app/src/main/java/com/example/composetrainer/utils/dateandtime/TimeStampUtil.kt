package com.example.composetrainer.utils.dateandtime

import saman.zamani.persiandate.PersianDate


object TimeStampUtil {

    fun getTodayAsTimestamp(): Long {
        return System.currentTimeMillis()
    }

    fun getTodayStartEndMillis(): Pair<Long, Long> {
        val today = PersianDate()
        val startOfDay = PersianDate().apply {
            setShDay(today.shDay)
            setShMonth(today.shMonth)
            setShYear(today.shYear)
            setHour(0)
            setMinute(0)
            setSecond(0)
        }

        val endOfDay = PersianDate().apply {
            setShDay(today.shDay)
            setShMonth(today.shMonth)
            setShYear(today.shYear)
            setHour(23)
            setMinute(59)
            setSecond(59)
        }

        return startOfDay.time to endOfDay.time
    }

    fun getCurrentShamsiMonthStartEndMillis(): Pair<Long, Long> {
        val now = PersianDate()

        // Start of current month
        val startOfMonth = PersianDate()
        startOfMonth.setShYear(now.shYear)
        startOfMonth.setShMonth(now.shMonth)
        startOfMonth.setShDay(1)
        startOfMonth.setHour(0)
        startOfMonth.setMinute(0)
        startOfMonth.setSecond(0)

        // End of current month
        val endOfMonth = PersianDate()
        endOfMonth.setShYear(now.shYear)
        endOfMonth.setShMonth(now.shMonth)
        endOfMonth.setShDay(now.monthDays) // Gets number of days in this month
        endOfMonth.setHour(23)
        endOfMonth.setMinute(59)
        endOfMonth.setSecond(59)

        return startOfMonth.time to endOfMonth.time
    }

    fun getCurrentShamsiWeekStartEndMillis(): Pair<Long, Long> {
        val now = PersianDate()
        val startOfWeek = PersianDate()
        startOfWeek.setShYear(now.shYear)
        startOfWeek.setShMonth(now.shMonth)
        // In Persian calendar, week starts on Saturday (1)
        val dayOfWeek = now.dayOfWeek() // 1=Saturday, 7=Friday
        startOfWeek.setShDay(now.shDay - (dayOfWeek - 1))
        startOfWeek.setHour(0)
        startOfWeek.setMinute(0)
        startOfWeek.setSecond(0)

        val endOfWeek = PersianDate()
        endOfWeek.setShYear(now.shYear)
        endOfWeek.setShMonth(now.shMonth)
        endOfWeek.setShDay(now.shDay + (7 - dayOfWeek))
        endOfWeek.setHour(23)
        endOfWeek.setMinute(59)
        endOfWeek.setSecond(59)

        // Handle week crossing month boundary
        if (startOfWeek.shDay < 1) {
            startOfWeek.setShMonth(now.shMonth - 1)
            val prevMonthDays = PersianDate().apply {
                setShYear(now.shYear)
                setShMonth(now.shMonth - 1)
            }.monthDays
            startOfWeek.setShDay(prevMonthDays + startOfWeek.shDay)
        }
        if (endOfWeek.shDay > now.monthDays) {
            endOfWeek.setShMonth(now.shMonth + 1)
            endOfWeek.setShDay(endOfWeek.shDay - now.monthDays)
        }

        return startOfWeek.time to endOfWeek.time
    }

    fun getLastShamsiWeekStartEndMillis(): Pair<Long, Long> {
        val now = PersianDate()
        val dayOfWeek = now.dayOfWeek() // 1=Saturday, 7=Friday

        // Start of last week
        val startOfLastWeek = PersianDate()
        startOfLastWeek.setShYear(now.shYear)
        startOfLastWeek.setShMonth(now.shMonth)
        startOfLastWeek.setShDay(now.shDay - (dayOfWeek - 1) - 7)
        startOfLastWeek.setHour(0)
        startOfLastWeek.setMinute(0)
        startOfLastWeek.setSecond(0)

        // End of last week
        val endOfLastWeek = PersianDate()
        endOfLastWeek.setShYear(now.shYear)
        endOfLastWeek.setShMonth(now.shMonth)
        endOfLastWeek.setShDay(now.shDay - (dayOfWeek - 1) - 1)
        endOfLastWeek.setHour(23)
        endOfLastWeek.setMinute(59)
        endOfLastWeek.setSecond(59)

        // Handle crossing month boundary for start
        while (startOfLastWeek.shDay < 1) {
            startOfLastWeek.setShMonth(startOfLastWeek.shMonth - 1)
            val prevMonthDays = PersianDate().apply {
                setShYear(startOfLastWeek.shYear)
                setShMonth(startOfLastWeek.shMonth)
            }.monthDays
            startOfLastWeek.setShDay(prevMonthDays + startOfLastWeek.shDay)
        }
        // Handle crossing month boundary for end
        while (endOfLastWeek.shDay < 1) {
            endOfLastWeek.setShMonth(endOfLastWeek.shMonth - 1)
            val prevMonthDays = PersianDate().apply {
                setShYear(endOfLastWeek.shYear)
                setShMonth(endOfLastWeek.shMonth)
            }.monthDays
            endOfLastWeek.setShDay(prevMonthDays + endOfLastWeek.shDay)
        }

        return startOfLastWeek.time to endOfLastWeek.time
    }

    fun getLastShamsiMonthStartEndMillis(): Pair<Long, Long> {
        val now = PersianDate()
        val lastMonth = if (now.shMonth == 1) 12 else now.shMonth - 1
        val yearOfLastMonth = if (now.shMonth == 1) now.shYear - 1 else now.shYear

        // Start of last month
        val startOfLastMonth = PersianDate()
        startOfLastMonth.setShYear(yearOfLastMonth)
        startOfLastMonth.setShMonth(lastMonth)
        startOfLastMonth.setShDay(1)
        startOfLastMonth.setHour(0)
        startOfLastMonth.setMinute(0)
        startOfLastMonth.setSecond(0)

        // End of last month
        val endOfLastMonth = PersianDate()
        endOfLastMonth.setShYear(yearOfLastMonth)
        endOfLastMonth.setShMonth(lastMonth)
        endOfLastMonth.setShDay(startOfLastMonth.monthDays)
        endOfLastMonth.setHour(23)
        endOfLastMonth.setMinute(59)
        endOfLastMonth.setSecond(59)

        return startOfLastMonth.time to endOfLastMonth.time
    }



    fun getYesterdayShamsiStartEndMillis(): Pair<Long, Long> {
        val now = PersianDate()
        val yesterday = PersianDate()
        yesterday.setShYear(now.shYear)
        yesterday.setShMonth(now.shMonth)
        yesterday.setShDay(now.shDay - 1)
        // Handle crossing month boundary
        if (yesterday.shDay < 1) {
            yesterday.setShMonth(now.shMonth - 1)
            if (yesterday.shMonth < 1) {
                yesterday.setShMonth(12)
                yesterday.setShYear(now.shYear - 1)
            }
            val prevMonthDays = PersianDate().apply {
                setShYear(yesterday.shYear)
                setShMonth(yesterday.shMonth)
            }.monthDays
            yesterday.setShDay(prevMonthDays)
        }

        val startOfYesterday = PersianDate()
        startOfYesterday.setShYear(yesterday.shYear)
        startOfYesterday.setShMonth(yesterday.shMonth)
        startOfYesterday.setShDay(yesterday.shDay)
        startOfYesterday.setHour(0)
        startOfYesterday.setMinute(0)
        startOfYesterday.setSecond(0)

        val endOfYesterday = PersianDate()
        endOfYesterday.setShYear(yesterday.shYear)
        endOfYesterday.setShMonth(yesterday.shMonth)
        endOfYesterday.setShDay(yesterday.shDay)
        endOfYesterday.setHour(23)
        endOfYesterday.setMinute(59)
        endOfYesterday.setSecond(59)

        return startOfYesterday.time to endOfYesterday.time
    }

}