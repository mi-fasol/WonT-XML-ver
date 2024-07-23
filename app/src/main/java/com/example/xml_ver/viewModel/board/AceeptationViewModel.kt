package com.example.xml_ver.viewModel.board

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.data.retrofit.acceptation.AcceptationModel
import com.example.xml_ver.data.retrofit.acceptation.AcceptationResponseModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.repository.AcceptationRepository
import com.example.xml_ver.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcceptationViewModel @Inject constructor(
    private val acceptationRepository: AcceptationRepository,
    private val context: Context
) : ViewModel() {

    private val _acceptationList = MutableStateFlow<List<AcceptationResponseModel>>(emptyList())
    val acceptationList: StateFlow<List<AcceptationResponseModel>> = _acceptationList

    private val _myAcceptState = MutableStateFlow(AcceptState.NONE)
    val myAcceptState: StateFlow<AcceptState> = _myAcceptState

    private val _attendeeList =
        MutableStateFlow<MutableMap<Int, List<UserResponseModel>>>(HashMap())
    val attendeeList: StateFlow<MutableMap<Int, List<UserResponseModel>>> = _attendeeList

    private val _attendeeModelList =
        MutableStateFlow<MutableMap<Int, List<AcceptationResponseModel>>>(HashMap())
    val attendeeModelList: StateFlow<MutableMap<Int, List<AcceptationResponseModel>>> =
        _attendeeModelList

    private val _acceptRegisterState =
        MutableStateFlow<Resource<AcceptationResponseModel>>(Resource.loading(null))
    val acceptRegisterState: StateFlow<Resource<AcceptationResponseModel>> =
        _acceptRegisterState.asStateFlow()

    private val _acceptDeleteState =
        MutableStateFlow<Resource<Boolean?>>(Resource.loading(null))
    val acceptDeleteState: StateFlow<Resource<Boolean?>> = _acceptDeleteState.asStateFlow()

    private val _allowUserState =
        MutableStateFlow<Resource<Boolean?>>(Resource.loading(null))
    val allowUserState: StateFlow<Resource<Boolean?>> = _allowUserState.asStateFlow()

    var nowAttendees = MutableStateFlow(0)

    suspend fun getAcceptationByPId(pId: Int) {
        _myAcceptState.value = AcceptState.NONE
        viewModelScope.launch {
            try {
                val response = acceptationRepository.getJoinUserByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    val currentList = _attendeeModelList.value.toMutableMap()
                    currentList[pId] = acceptList ?: listOf()
                    _attendeeModelList.value = currentList
                    _attendeeModelList.value[pId]?.forEach {
                        if (it.uId == SharedPreferenceUtil(context).getInt("uId", 0)) {
                            _myAcceptState.value = if (it.acceptation) {
                                AcceptState.JOIN
                            } else {
                                AcceptState.REQUEST
                            }
                        }
                    }

                    Log.d("미란 참여 테이블", _attendeeModelList.value[pId].toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getAttendeeByPId(pId: Int) {
        viewModelScope.launch {
            try {
                val response = acceptationRepository.getAttendeeListByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    _attendeeList.value[pId] = acceptList!!
                    Log.d("미란 참여 유저", _attendeeList.value[pId].toString())
                    if (_attendeeList.value[pId] != null) {
                        nowAttendees.value = _attendeeList.value[pId]!!.size
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun changeAcceptationRequest(pId: Int) {
        if(_myAcceptState.value == AcceptState.NONE) {
            Log.d("미란", "요기")
            sendAcceptationRequest(pId)
        } else {
            Log.d("미란", "죠기")
            deleteAcceptationRequest(pId)
        }
    }

    private fun sendAcceptationRequest(pId: Int) {
        val accept = AcceptationModel(
            pId = pId,
            uId = SharedPreferenceUtil(context).getInt("uId", 0),
            acceptation = false
        )

        viewModelScope.launch {
            _acceptRegisterState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.registerAcceptRequest(accept)
                if (response.isSuccessful && response.body() != null) {
                    _acceptRegisterState.value = Resource.success(response.body())
                    Log.d("참여 요청 완료", response.body().toString())
                    _myAcceptState.value = AcceptState.REQUEST
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    private fun deleteAcceptationRequest(pId: Int) {
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)
        viewModelScope.launch {
            _acceptDeleteState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.deleteAcceptRequest(uId, pId)
                if (response.isSuccessful && response.body() != null) {
                    _acceptDeleteState.value = Resource.success(response.body())
                    _myAcceptState.value = AcceptState.NONE
                    Log.d("참여 요청 완료", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun allowUserToJoin(pId: Int, uId: Int) {
        viewModelScope.launch {
            _allowUserState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.allowUserToJoin(uId, pId)
                if (response.isSuccessful && response.body() != null) {
                    _allowUserState.value = Resource.success(response.body())
                    Log.d("참여 요청 완료", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }
}