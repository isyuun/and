package kr.carepet._app

import android.os.Handler
import android.os.Looper
import android.os.Message

open class appcompatactivity2 : appcompatactivity() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    fun post(d: Long = 0, r: Runnable) {
        handler.postDelayed(r, d)
    }

    fun remove(r: Runnable) {
        handler.removeCallbacks(r)
    }

    fun send(d: Long = 0, m: Message) {
        handler.sendMessageDelayed(m, d)
    }

    fun handle(m: Message) {
        handler.handleMessage(m)
    }

    fun dispatch(m: Message) {
        handler.dispatchMessage(m)
    }
}