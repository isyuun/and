package net.pettip.data.daily

import android.location.Location
import android.net.Uri
import net.pettip.data.pet.CurrentPetData
import net.pettip.gpx.TRACK_ZERO_NUM
import net.pettip.gpx.TRACK_ZERO_URI
import net.pettip.gpx.Track

/**
 * @Project     : PetTip-Android
 * @FileName    : UploadInfo
 * @Date        : 2024-04-22
 * @author      : CareBiz
 * @description : net.pettip.data.daily
 * @see net.pettip.data.daily.UploadInfo
 */
data class UploadInfo(
 val file: String?,
 val uriList: List<String>,
 val cmntUseYn: String,// 댓글 사용 여부
 val hashTag: String,
 val pet: List<Pet>,
 val rcmdtnYn: String, // 추천 여부
 val rlsYn: String,
 val schCdList: List<String>,
 val schTtl: String,
 val schCn: String?,
 val totClr: Float,
 val totDstnc: Float,
 val walkDptreDt: String,
 val walkEndDt: String,
 val pets: List<CurrentPetData>?,
 val totDuration: String?,
 val tracks : MutableList<UriTrack>?
)

data class UriTrack(
 private val loc: Location,
 val no: String = TRACK_ZERO_NUM,
 val event: net.pettip.gpx.Track.EVENT = net.pettip.gpx.Track.EVENT.NNN
) {
 companion object {

 }

 fun toText(): String {
  return "($latitude, $longitude)"
 }

 val location: Location
  get() = loc

 val latitude: Double
  get() = loc.latitude

 val longitude: Double
  get() = loc.longitude

 val time: Long
  get() = loc.time

 val speed: Float
  get() = loc.speed

 val altitude: Double
  get() = loc.altitude

 val bearing: Float
  get() = loc.bearing
}

