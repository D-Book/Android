package kr.hs.dgsw.dbook.ui.module

import android.icu.text.SimpleDateFormat
import java.util.*

class FeedTime {

    private object TimeData {
        const val SEC = 60
        const val MIN = 60
        const val HOUR = 24
        const val DAY = 30
        const val MONTH = 12
    }

    fun calFeedTime(time: String): String{

        var serverTimeString = ""
        val timeArray: ArrayList<String> = time.split("T") as ArrayList<String>

        for(i in 0 until timeArray.size){
            serverTimeString += if(i == 0){
                timeArray[i] + " "
            }else{
                timeArray[i]
            }
        }

        val myDate = serverTimeString
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date = sdf.parse(myDate)
        val millis: Long = date.time

        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - millis) / 1000
        val msg: String

        when {
            diffTime < TimeData.SEC -> {
                msg = "방금 전"
            }
            TimeData.SEC.let { diffTime /= it; diffTime } < TimeData.MIN -> {
                msg = "$diffTime minutes ago"
            }
            TimeData.MIN.let { diffTime /= it; diffTime } < TimeData.HOUR -> {
                msg = "$diffTime hours ago"
            }
            TimeData.HOUR.let { diffTime /= it; diffTime } < TimeData.DAY -> {
                msg = "$diffTime days ago"
            }
            TimeData.DAY.let { diffTime /= it; diffTime } < TimeData.MONTH -> {
                msg = "$diffTime months ago"
            }
            else -> {
                msg = "$diffTime years ago"
            }
        }
        return msg

    }

    fun calFeedTimeComment(time: String): String{

        var serverTimeString = ""
        val timeArray: ArrayList<String> = time.split("T") as ArrayList<String>

        for(i in 0 until timeArray.size){
            serverTimeString += if(i == 0){
                timeArray[i] + " "
            }else{
                timeArray[i]
            }
        }

        val myDate = serverTimeString
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date = sdf.parse(myDate)
        val millis: Long = date.time

        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - millis) / 1000
        val msg: String

        when {
            diffTime < TimeData.SEC -> {
                msg = "방금 전"
            }
            TimeData.SEC.let { diffTime /= it; diffTime } < TimeData.MIN -> {
                msg = "$diffTime 분 전"
            }
            TimeData.MIN.let { diffTime /= it; diffTime } < TimeData.HOUR -> {
                msg = "$diffTime 시간 전"
            }
            TimeData.HOUR.let { diffTime /= it; diffTime } < TimeData.DAY -> {
                msg = "$diffTime 일 전"
            }
            TimeData.DAY.let { diffTime /= it; diffTime } < TimeData.MONTH -> {
                msg = "$diffTime 달 전"
            }
            else -> {
                msg = "$diffTime 년 전"
            }
        }
        return msg

    }
}