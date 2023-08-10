package kr.carepet._app

import android.os.Handler
import android.os.Looper
import android.os.Message

open class appcompatactivity2 : appcompatactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val handler: Handler = Handler(Looper.getMainLooper())

    fun post(r: Runnable, d: Long = 0) {
        handler.postDelayed(r, d)
    }

    fun remove(r: Runnable) {
        handler.removeCallbacks(r)
    }

    fun send(m: Message, d: Long = 0) {
        handler.sendMessageDelayed(m, d)
    }

    fun handle(m: Message) {
        handler.handleMessage(m)
    }

    fun dispatch(m: Message) {
        handler.dispatchMessage(m)
    }
}