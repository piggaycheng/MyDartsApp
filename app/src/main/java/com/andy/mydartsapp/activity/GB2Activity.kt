package com.andy.mydartsapp.activity

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.andy.mydartsapp.R
import com.andy.mydartsapp.util.MyUtil

class GB2Activity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE_ALL = 1
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gb2)

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE_ALL)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE_ALL -> {
                for(grantResult: Int in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        finish()
                    }
                }
            }
        }
    }
}
