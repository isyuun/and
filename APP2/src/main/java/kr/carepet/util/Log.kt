package kr.carepet.util

class Log {
    companion object {
        const val ASSERT = 7
        const val DEBUG = 3
        const val ERROR = 6
        const val INFO = 4
        const val VERBOSE = 2
        const val WARN = 5

        private fun Log() { /* compiled code */
        }

        fun v(tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.v(tag, msg)
        }

        fun v(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.v(tag, msg, tr)
        }

        fun d(tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.d(tag, msg)
        }

        fun d(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.d(tag, msg, tr)
        }

        fun i(tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.i(tag, msg)
        }

        fun i(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.i(tag, msg, tr)
        }

        fun w(tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.w(tag, msg)
        }

        fun w(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.w(tag, msg, tr)
        }

        external fun isLoggable(s: String?, i: Int): Boolean

        fun w(tag: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.w(tag, tr)
        }

        fun e(tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.e(tag, msg)
        }

        fun e(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.e(tag, msg, tr)
        }

        fun wtf(tag: String?, msg: String?): Int { /* compiled code */
            return android.util.Log.wtf(tag, msg)
        }

        fun wtf(tag: String?, tr: Throwable): Int { /* compiled code */
            return android.util.Log.wtf(tag, tr)
        }

        fun wtf(tag: String?, msg: String?, tr: Throwable?): Int { /* compiled code */
            return android.util.Log.wtf(tag, msg, tr)
        }

        fun getStackTraceString(tr: Throwable?): String { /* compiled code */
            return android.util.Log.getStackTraceString(tr)
        }

        fun println(priority: Int, tag: String?, msg: String): Int { /* compiled code */
            return android.util.Log.println(priority, tag, msg)
        }

    }
}