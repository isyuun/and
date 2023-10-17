/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 29.   description...
 */

package kr.carepet.util

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

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
        private val DEBUG = kr.carepet.BuildConfig.DEBUG

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
