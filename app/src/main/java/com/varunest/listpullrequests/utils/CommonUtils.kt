package com.varunest.listpullrequests.utils

import android.content.Context
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.support.v4.content.ContextCompat.getSystemService
import android.text.format.DateUtils
import java.util.*


object CommonUtils {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun currentDateUTC(): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        return calendar.time
    }

    fun getTimeAgo(date: Date): String {
        var time = date.time
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return "in the future"
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "moments ago"
            diff < 2 * MINUTE_MILLIS -> "a minute ago"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
            diff < 2 * HOUR_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            else -> "${diff / DAY_MILLIS} days ago"
        }
    }

}