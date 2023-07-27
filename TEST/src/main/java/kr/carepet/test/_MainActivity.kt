package kr.carepet.test

import android.os.Bundle
import kr.carepet.util.Log

class _MainActivity : MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
        setContentView(0)       //IY:test-일부러.죽여봄
    }
}