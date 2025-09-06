package com.example.tempui_0821

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import com.example.tempservice.ITemperatureService


class MainActivity : ComponentActivity() {
    private var getTempFunc: (() -> Int)? = null

    private val conn = object : ServiceConnection {
        private var service: ITemperatureService? = null
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = ITemperatureService.Stub.asInterface(binder)
            getTempFunc = { service?.getCurrentTemperature() ?: 0 }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            getTempFunc = null
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent()
        intent.setClassName(this, "com.example.tempservice.TemperatureService")
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        try { unbindService(conn) } catch (_: Exception) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TempScreen(
            getTemp = { getTempFunc?.invoke() ?: 0 }
        ) }
    }
}

@Composable
fun TempScreen(getTemp: () -> Int) {
    var tempC by remember { mutableStateOf(0) }
    var useFahrenheit by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            tempC = getTemp()
            delay(1000)
        }
    }

    val display = if (useFahrenheit) {
        "%.1f °F".format(tempC * 9.0 / 5 + 32)
    } else {
        "$tempC °C"
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(display, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { useFahrenheit = !useFahrenheit }) {
            Text("Toggle °C / °F")
        }
    }
}
