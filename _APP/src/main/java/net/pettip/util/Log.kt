/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 8. 29.   description...
 */

package net.pettip.util

import net.pettip.DEBUG

/** getMethodLine() */
fun getMethodName(): String {
    val stack = Thread.currentThread().stackTrace[3]
    //val path = "${stack.className}::${stack.methodName}"
    val path = "::${stack.methodName}"
    return "${path}(${stack.fileName}:${stack.lineNumber})"
}

class Log {
    companion object {
        //const val ASSERT = 7
        //const val DEBUG = 3
        //const val ERROR = 6
        //const val INFO = 4
        //const val VERBOSE = 2
        //const val WARN = 5

        private fun Log() { /* compiled code */
        }

        fun v(tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.v(tag, msg) else -1
        }

        fun v(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.v(tag, msg, tr) else -1
        }

        fun d(tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.d(tag, msg) else -1
        }

        fun d(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.d(tag, msg, tr) else -1
        }

        fun i(tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.i(tag, msg) else -1
        }

        fun i(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.i(tag, msg, tr) else -1
        }

        fun w(tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.w(tag, msg) else -1
        }

        fun w(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.w(tag, msg, tr) else -1
        }

        external fun isLoggable(s: String?, i: Int): Boolean

        fun w(tag: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.w(tag, tr) else -1
        }

        fun e(tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.e(tag, msg) else -1
        }

        fun e(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.e(tag, msg, tr) else -1
        }

        fun wtf(tag: String?, msg: String?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.wtf(tag, msg) else -1
        }

        fun wtf(tag: String?, tr: Throwable): Int { /* compiled code */
            return if (DEBUG) android.util.Log.wtf(tag, tr) else -1
        }

        fun wtf(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return if (DEBUG) android.util.Log.wtf(tag, msg, tr) else -1
        }

        fun getStackTraceString(tr: Throwable?): String { /* compiled code */
            return if (DEBUG) android.util.Log.getStackTraceString(tr) else ""
        }

        fun println(priority: Int, tag: String?, msg: String): Int { /* compiled code */
            return if (DEBUG) android.util.Log.println(priority, tag, msg) else -1
        }

    }
}
