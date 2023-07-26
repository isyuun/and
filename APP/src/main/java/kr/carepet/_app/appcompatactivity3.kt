package kr.carepet._app

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kr.carepet.util.log

open class appcompatactivity3 : appcompatactivity2() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        log.i(__CLASSNAME__, "${toString()}:${getMethodName()}${savedInstanceState},${persistentState}")
        root = findViewById<View>(android.R.id.content).rootView
        root = window.decorView.findViewById(android.R.id.content)
    }

    private lateinit var toast: Toast

    fun toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        toast.cancel()
        toast = Toast.makeText(this, text, duration)
        toast.show()
    }

    private lateinit var snackbar: Snackbar

    fun snack(text: CharSequence, duration: Int) {
        snackbar = Snackbar.make(root, text, duration)
        snackbar.show()
    }
}