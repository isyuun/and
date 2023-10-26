package kr.carepet.app.navi.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.carepet.data.daily.WeekData
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.data.pet.PetDetailData

class HomeViewModel(private val sharedViewModel: SharedViewModel):ViewModel() {

    val weekRecord: StateFlow<WeekData?> = sharedViewModel.weekRecord
    fun updatePetInfo(newData: List<PetDetailData>){
        sharedViewModel.updatePetInfo(newData)
    }

    fun updateCurrentPetInfo(newData: List<CurrentPetData>){
        sharedViewModel.updateCurrentPetInfo(newData)
    }

    fun updateSeletedPet(newData: CurrentPetData){
        sharedViewModel.updateSelectPet(newData)
    }

    suspend fun getWeekRecord(ownrPetUnqNo: String, searchDay: String){
        sharedViewModel.getWeekRecord(ownrPetUnqNo, searchDay)
    }

    fun changeBirth(birth:String):String{
        return sharedViewModel.changeBirth(birth)
    }

    val emptyPet = PetDetailData(
        ownrPetUnqNo = "",
        petBrthYmd = "미상",
        petInfoUnqNo = 0,
        petKindNm = "웨스트 하이랜드 화이트 테리어",
        petMngrYn = "",
        petNm = "배추",
        petRegNo = "",
        petRelCd = "",
        petRelNm = "",
        petRelUnqNo = 0,
        petRprsImgAddr = "",
        petRprsYn = "Y",
        sexTypCd = "",
        sexTypNm = "수컷",
        stdgCtpvCd = "",
        stdgCtpvNm = "",
        stdgSggCd = "",
        stdgSggNm = "",
        stdgUmdCd = "",
        stdgUmdNm = "",
        wghtVl = 0.0f,
        ntrTypCd = "",
        ntrTypNm = "",
        endDt = "",
        mngrType = "",
        memberList = emptyList()
    )

    val emptyCurrentPet = CurrentPetData(
        age = "0살",
        petNm = "펫을 등록해주세요",
        ownrPetUnqNo = "",
        petKindNm = "",
        petRprsImgAddr = "",
        sexTypNm = "",
        wghtVl =0.0f
    )

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()
    fun updateShowBottomSheet(newValue: Boolean) { _showBottomSheet.value = newValue }

    val currentPetInfo = sharedViewModel.currentPetInfo

    val petInfo = sharedViewModel.petInfo

    private val _repPet = MutableStateFlow<List<PetDetailData>>(emptyList())
    val repPet: StateFlow<List<PetDetailData>> = _repPet.asStateFlow()

    val isLoading = MutableStateFlow<Boolean>(true)
    fun updateIsLoading(newValue: Boolean){isLoading.value = newValue}

    private val _selectPetManage = MutableStateFlow<PetDetailData?>(null)
    val selectPetManage: StateFlow<PetDetailData?> = _selectPetManage.asStateFlow()
    fun updateSelectPetManage(newValue: PetDetailData?) { _selectPetManage.value = newValue }

    private val _petListSelectIndex = MutableStateFlow("0")
    val petListSelectIndex: StateFlow<String> = _petListSelectIndex.asStateFlow()
    fun updatePetListSelectIndex(newValue: String) { _petListSelectIndex.value = newValue }

}
