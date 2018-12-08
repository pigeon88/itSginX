package com.pigeon.it.sginx

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button = findViewById<View>(R.id.button) as Button
        if (ConfigActivity.getConfigTime(this) > 0) {
            var configTime = ConfigActivity.getConfigTime(this)
            val respDate = SimpleDateFormat("yyyy-MM-dd").format(Date(configTime))
            button.text = respDate
        }
        button.setOnClickListener {
            val editText = findViewById<TextView>(R.id.textView)
            AsyncTask.execute {
                /*ITalentSign.httpRequest("https://www.italent.cn/api/v1/106454/111835773/Signin/AddV4") { responseDate, responseText, responseCode ->
                    Handler(Looper.getMainLooper()).post {
                        val respDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(responseDate))
                        editText.text = ("$respDate -> $responseCode\r\n$responseText")
                    }
                }*/

                var configContext = ConfigActivity.getConfigText(this@MainActivity)
                if (configContext == null) {
                    startActivity(Intent(this, ConfigActivity::class.java))
                    return@execute
                }

                var configUrl = ConfigActivity.getConfigUrl(this@MainActivity)
                ITalentSign.httpRequestRaw(configUrl, configContext) { responseDate, responseText, responseCode ->
                    Handler(Looper.getMainLooper()).post {
                        val respDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(responseDate))
                        editText.text = ("$respDate -> $responseCode\r\n$responseText")
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_config -> startActivity(Intent(this, ConfigActivity::class.java))
        }
        return true
    }
}
