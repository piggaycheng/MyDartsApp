package com.andy.mydartsapp.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class MyUtil {
    companion object {
        fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
            for (permission: String in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false
                }
            }
            return true
        }
    }
}