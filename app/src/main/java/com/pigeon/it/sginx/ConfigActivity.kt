package com.pigeon.it.sginx

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_config.*
import java.util.*

class ConfigActivity : Activity() {

    companion object {
        const val CONFIG_SAVE_TIME = "http:config_time"
        const val CONFIG_URL = "http:config_url"
        const val CONFIG_TEXT = "http:config_text"

        fun getConfigTime(context: Context):Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(CONFIG_SAVE_TIME, -1)
        }

        fun getConfigUrl(context: Context): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(CONFIG_URL, null)
                    ?: "https://www.italent.cn/api/v1/106454/111835773/Signin/AddV4"
        }

        fun getConfigText(context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(CONFIG_TEXT, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        et_url.setText(getConfigUrl(this))
        et_url.setSelection(et_url.text.length)
        et_content.setText(getConfigText(this))

        findViewById<View>(R.id.button).setOnClickListener {
            //val etUrl = findViewById<EditText>(R.id.et_url)
            //val editText = findViewById<TextView>(R.id.config_edt)
            //AsyncTask.execute {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            preferences.edit()
                    .putLong(CONFIG_SAVE_TIME, Date().time)
                    .putString(CONFIG_URL, et_url.text.toString())
                    .putString(CONFIG_TEXT, et_content.text.toString())
                    .apply()
            Toast.makeText(this@ConfigActivity, "保存成功", Toast.LENGTH_SHORT).show()
            //}
        }
    }
}
