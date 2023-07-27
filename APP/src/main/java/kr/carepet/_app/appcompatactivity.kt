package kr.carepet._app

import android.os.Bundle
import android.os.PersistableBundle
import kr.carepet.util.log

open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    protected open fun getMethodName(): String? {
        //var name = Thread.currentThread().stackTrace[3].methodName
        //var line = Thread.currentThread().stackTrace[3].lineNumber;
        //name = String.format("line:%d - %s() ", line, name);
        //name += "() "
        val name = javaClass.simpleName
        val line = Thread.currentThread().stackTrace[3].lineNumber;
        return "${Thread.currentThread().stackTrace[3].methodName}(${name}.kt:${line})"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log.i(__CLASSNAME__, "${getMethodName()}${savedInstanceState}")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        log.i(__CLASSNAME__, "${getMethodName()}${savedInstanceState},${persistentState}")
    }
}
