package com.example.tempservice

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TemperatureService : Service() {
    private val binder = object : ITemperatureService.Stub() {
        override fun getCurrentTemperature(): Int {
            val raw = getSystemProperty("persist.sys.temp.current", "0")
            return raw.toIntOrNull() ?: 0
        }

        override fun getCurrentTemperatureUnit(): String {
            return "°C"
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun getSystemProperty(key: String, def: String): String {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod("get", String::class.java, String::class.java)
            getMethod.invoke(null, key, def) as String
        } catch (e: Exception) {
            def
        }
    }
}
