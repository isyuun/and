package net.pettip.app.navi.component

import androidx.compose.runtime.Composable
import net.pettip.app.navi.R

/**
 * @Project     : PetTip-Android
 * @FileName    : DefaultPetImage
 * @Date        : 2024-05-08
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.DefaultPetImage
 */

fun defaultPetImage(petType:String?):Int{
    return when(petType){
        "001" -> R.drawable.profile_default
        "002" -> R.drawable.cat_profile2
        else -> R.drawable.profile_default
    }
}