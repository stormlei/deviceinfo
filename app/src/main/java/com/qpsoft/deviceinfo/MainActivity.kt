package com.qpsoft.deviceinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.qpsoft.deviceinfo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(null))
                .commitNow()
        }
    }

    override fun onBackPressed() {
        exitApp()
    }

    private var exitTime: Long = 0

    private fun exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出应用程序", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}