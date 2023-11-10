package kr.carepet.app.navi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kr.carepet.gps.app.GPSApplication
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    val application = GPSApplication.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onStart()
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onResume()
        if (!application.start) {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onNewIntent(intent)
    }
}