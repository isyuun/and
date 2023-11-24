package net.pettip._app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

open class appcompatactivity3 : appcompatactivity2() {
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun snack(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
        snackbar = Snackbar.make(root, text, duration)
        snackbar.show()
    }
}