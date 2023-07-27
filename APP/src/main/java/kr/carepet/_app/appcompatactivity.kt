package kr.carepet._app

import android.os.Bundle
import android.os.PersistableBundle
import kr.carepet.util.Log

open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    protected open fun getMethodName(): String? {
        val name = Thread.currentThread().stackTrace[3].methodName
        val path = "${javaClass.name}.${name}"
        val file = javaClass.simpleName
        val line = Thread.currentThread().stackTrace[3].lineNumber
        return "${path}(${file}.kt:${line}) "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(__CLASSNAME__, "${getMethodName()}${savedInstanceState}")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i(__CLASSNAME__, "${getMethodName()}${savedInstanceState},${persistentState}")
    }
}
