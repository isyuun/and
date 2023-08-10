package kr.carepet._app

import android.os.Bundle
import kr.carepet.util.Log

open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    protected open fun getMethodName(): String? {
        val stack = Thread.currentThread().stackTrace[3]
        val className = stack.className
        val methodName = stack.methodName
        val path = "${className}.${methodName}"
        val file = stack.fileName
        val line = stack.lineNumber
        return "${path}(${file}:${line}) "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
    }
}
