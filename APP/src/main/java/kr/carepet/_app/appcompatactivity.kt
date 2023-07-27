package kr.carepet._app

import android.os.Bundle
import kr.carepet.util.Log

open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    protected open fun getMethodName(): String? {
        val className = Thread.currentThread().stackTrace[3].className
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val path = "${className}.${methodName}"
        val file = Thread.currentThread().stackTrace[3].fileName
        val line = Thread.currentThread().stackTrace[3].lineNumber
        return "${path}(${file}:${line}) "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
    }
}
