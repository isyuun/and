package kr.carepet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.carepet.data.pet.CurrentPetData

class MapRepository {

    private val petInfo = MutableLiveData<List<CurrentPetData>>()

    fun getPetInfo(): LiveData<List<CurrentPetData>> {
        return petInfo
    }

    fun updatePetInfo(newData: List<CurrentPetData>) {
        petInfo.value = newData
    }
}
