package net.pettip.data

import androidx.compose.runtime.Immutable
import javax.inject.Inject

@Immutable
data class SCD(
    val cdld: String?,
    val upCdId: String?,
    val cdNm: String?
)

class SCDLocalData @Inject constructor() {
    val scd = listOf( // 시도코드
        SCD(cdld = "36", upCdId = "SCD", cdNm = "세종특별자치시"),
        SCD(cdld = "50", upCdId = "SCD", cdNm = "제주특별자치도"),
        SCD(cdld = "48", upCdId = "SCD", cdNm = "경상남도"),
        SCD(cdld = "47", upCdId = "SCD", cdNm = "경상북도"),
        SCD(cdld = "46", upCdId = "SCD", cdNm = "전라남도"),
        SCD(cdld = "45", upCdId = "SCD", cdNm = "전라북도"),
        SCD(cdld = "44", upCdId = "SCD", cdNm = "충청남도"),
        SCD(cdld = "43", upCdId = "SCD", cdNm = "충청북도"),
        SCD(cdld = "41", upCdId = "SCD", cdNm = "경기도"),
        SCD(cdld = "30", upCdId = "SCD", cdNm = "대전광역시"),
        SCD(cdld = "29", upCdId = "SCD", cdNm = "광주광역시"),
        SCD(cdld = "28", upCdId = "SCD", cdNm = "인천광역시"),
        SCD(cdld = "27", upCdId = "SCD", cdNm = "대구광역시"),
        SCD(cdld = "26", upCdId = "SCD", cdNm = "부산광역시"),
        SCD(cdld = "11", upCdId = "SCD", cdNm = "서울특별시"),
        SCD(cdld = "51", upCdId = "SCD", cdNm = "강원특별자치도")
    )
}
