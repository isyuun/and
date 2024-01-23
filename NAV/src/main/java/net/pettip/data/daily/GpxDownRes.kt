package net.pettip.data.daily

import com.google.gson.annotations.SerializedName

/**
 * @Project     : PetTip-Android
 * @FileName    : GpxDownRes
 * @Date        : 2024-01-23
 * @author      : CareBiz
 * @description : net.pettip.data.daily
 * @see net.pettip.data.daily.GpxDownRes
 */
data class GpxDownRes(
    @SerializedName("totMvmnPathFileSn")
    var totMvmnPathFileSn: String
)
