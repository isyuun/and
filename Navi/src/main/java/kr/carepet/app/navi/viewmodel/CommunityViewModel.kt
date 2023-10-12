package kr.carepet.app.navi.viewmodel

import androidx.lifecycle.ViewModel
import com.bumptech.glide.annotation.GlideModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@GlideModule
class CommunityViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    private val _comment = MutableStateFlow<String>("")
    val comment: StateFlow<String> = _comment.asStateFlow()
    fun updateComment(newValue: String) { _comment.value = newValue }
}