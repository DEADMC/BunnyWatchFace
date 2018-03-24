package app.deadmc.toxicwatchface

import java.util.*

/**
 * Created by DEADMC on 3/24/2018.
 */
class TimeHelper() {
    val calendar = Calendar.getInstance()

    fun refresh() {
        calendar.timeInMillis = System.currentTimeMillis()
    }

    fun getText():String {
        val text = String.format("%d:%02d", calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE))
        return text
    }

    fun getSecond():Int = calendar.get(Calendar.SECOND)

    fun getMinute():Int = calendar.get(Calendar.MINUTE)

    fun getHour():Int = calendar.get(Calendar.HOUR_OF_DAY)




}