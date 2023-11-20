/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.9
 *
 */

package kr.carepet

/**
 * @Project     : PetTip-Android
 * @FileName    : config
 * @Date        : 2023-11-09
 * @author      : isyuun
 * @description : kr.carepet
 * @see kr.carepet.config
 */
//private val __CLASSNAME__ = Exception().stackTrace[0].fileName
val DEBUG = BuildConfig.DEBUG
//val DEBUG = false
val RELEASE = true and !BuildConfig.DEBUG
