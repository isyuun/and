/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.carepet.whileinuselocation

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

/**
 *  This app allows a user to receive location updates without the background permission even when
 *  the app isn't in focus. This is the preferred approach for Android.
 *
 *  It does this by creating a foreground service (tied to a Notification) when the
 *  user navigates away from the app. Because of this, it only needs foreground or "while in use"
 *  location permissions. That is, there is no need to ask for location in the background (which
 *  requires additional permissions in the manifest).
 *
 *  Note: Users have the following options in Android 11+ regarding location:
 *
 *  * Allow all the time
 *  * Allow while app is in use, i.e., while app is in foreground (new in Android 10)
 *  * Allow one time use (new in Android 11)
 *  * Not allow location at all
 *
 * It is generally recommended you only request "while in use" location permissions (location only
 * needed in the foreground), e.g., fine and coarse. If your app has an approved use case for
 * using location in the background, request that permission in context and separately from
 * fine/coarse location requests. In addition, if the user denies the request or only allows
 * "while-in-use", handle it gracefully. To see an example of background location, please review
 * {@link https://github.com/android/location-samples/tree/master/LocationUpdatesBackgroundKotlin}.
 *
 * Android 10 and higher also now requires developers to specify foreground service type in the
 * manifest (in this case, "location").
 *
 * For the feature that requires location in the foreground, this sample uses a long-running bound
 * and started service for location updates. The service is aware of foreground status of this
 * activity, which is the only bound client in this sample.
 *
 * While getting location in the foreground, if the activity ceases to be in the foreground (user
 * navigates away from the app), the service promotes itself to a foreground service and continues
 * receiving location updates.
 *
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 *
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
class MainActivity : com.example.android.whileinuselocation.MainActivity() {
}
