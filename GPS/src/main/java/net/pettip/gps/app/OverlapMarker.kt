package net.pettip.gps.app

import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import net.pettip.gps.R

/**
 * @Project     : PetTip-Android
 * @FileName    : OverlapMarker
 * @Date        : 2024-05-08
 * @author      : CareBiz
 * @description : net.pettip.gps.app
 * @see net.pettip.gps.app.OverlapMarker
 */
val markers = listOf<Marker>(

)

// 마커 겹침을 확인하고 처리하는 함수
fun checkAndHandleMarkerOverlap() {
    val tolerance = 10 // 겹침 허용 오차
    val markerPositions = markers.map { it.position }
    val overlappedGroups = mutableListOf<List<Marker>>() // 겹치는 마커들을 묶은 리스트

    markers.forEach { marker ->
        val overlapped = markers.filter { other ->
            other != marker && marker.position.distanceTo(other.position) < tolerance
        }
        if (overlapped.isNotEmpty()) {
            overlappedGroups.add(listOf(marker) + overlapped)
        }
    }

    // 겹친 그룹의 마커들을 처리
    overlappedGroups.forEach { group ->
        handleOverlappedMarkers(group)
    }
}

// 겹친 마커들을 처리하는 함수
fun handleOverlappedMarkers(group: List<Marker>) {
    group.forEach { marker ->
        marker.icon = OverlayImage.fromResource(R.drawable.ic_cancel) // 겹침 표시 아이콘으로 변경
        marker.zIndex = 100 // 겹친 마커들의 z-index를 높여서 상위에 표시
        //marker.map = map // 마커를 맵에 다시 추가하여 업데이트
    }
}