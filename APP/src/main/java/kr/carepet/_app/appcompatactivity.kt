package kr.carepet._app

import android.os.Bundle
import android.os.PersistableBundle
import kr.carepet.util.log

open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun toString(): String {
        return javaClass.simpleName + '@' + Integer.toHexString(hashCode())
    }

    protected open fun getMethodName(): String? {
        var name = Thread.currentThread().stackTrace[3].methodName
        //var line = Thread . currentThread ().stackTrace[3].lineNumber;
        //name = String.format("line:%d - %s() ", line, name);
        name += "() "
        return name
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        log.i(__CLASSNAME__, "${toString()}:${getMethodName()}${savedInstanceState},${persistentState}")
    }
}
