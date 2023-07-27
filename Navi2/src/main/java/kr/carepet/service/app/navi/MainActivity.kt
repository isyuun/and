package kr.carepet.service.app.navi

import android.os.Bundle
import kr.carepet.app.AppCompatActivity
import kr.carepet.util.Log

open class MainActivity : AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}