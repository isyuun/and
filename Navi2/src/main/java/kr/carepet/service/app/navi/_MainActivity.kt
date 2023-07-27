package kr.carepet.service.app.navi

import android.os.Bundle
import kr.carepet.util.log

class _MainActivity : MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log.w(__CLASSNAME__, "${getMethodName()}${savedInstanceState}")
        setContentView(0)
    }
}