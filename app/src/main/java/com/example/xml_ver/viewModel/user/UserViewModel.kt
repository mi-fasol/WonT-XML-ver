package com.example.xml_ver.viewModel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.data.retrofit.user.UserModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.repository.UserRepository
import com.example.xml_ver.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val _registerState =
        MutableStateFlow<Resource<UserResponseModel>>(Resource.loading(null))
    val registerState: StateFlow<Resource<UserResponseModel>> = _registerState.asStateFlow()

    private val _fetchUserState =
        MutableStateFlow<Resource<UserResponseModel>>(Resource.loading(null))
    val fetchUserState: StateFlow<Resource<UserResponseModel>> = _fetchUserState.asStateFlow()

    private val _user = MutableStateFlow<UserResponseModel?>(null)
    val user: StateFlow<UserResponseModel?> = _user

    private val _userByNickname = MutableStateFlow<UserResponseModel?>(null)
    val userByNickname: StateFlow<UserResponseModel?> = _userByNickname

    private val _otherPerson = MutableStateFlow<UserResponseModel?>(null)
    val otherPerson: StateFlow<UserResponseModel?> = _otherPerson

    private val _isRegisterSuccess = MutableStateFlow<Boolean>(false)
    val isRegisterSuccess: StateFlow<Boolean> = _isRegisterSuccess.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow<Boolean>(false)
    val isDeleteSuccess: StateFlow<Boolean> = _isDeleteSuccess.asStateFlow()

    val image = MutableStateFlow(0)
    val nickname = MutableStateFlow("")
    val gender = MutableStateFlow("")
    val major = MutableStateFlow("")

    var isValid: StateFlow<Boolean> = combine(nickname, gender, major) { nickname, gender, major ->
        nickname.isNotBlank() && gender.isNotBlank() && major.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun registerUser(nickname: String, gender: String) {
        val studentId = SharedPreferenceUtil(context).getString("studentId", "").toString().toInt()
        val user = UserModel(nickname, studentId, major.value, gender, image.value)

        viewModelScope.launch {
            _registerState.value = Resource.loading(null)
            try {
                val response = repository.registerUser(user)
                if (response.isSuccessful && response.body() != null) {
                    val responseUser = response.body()
                    _registerState.value = Resource.success(response.body())
                    _isRegisterSuccess.value = true
                    SharedPreferenceUtil(context).setUser(responseUser!!)
                    SharedPreferenceUtil(context).setInt("uId", responseUser.uId)
                    SharedPreferenceUtil(context).setInt("image", responseUser.userImage)
                    SharedPreferenceUtil(context).setString("gender", responseUser.gender)
                    SharedPreferenceUtil(context).setString("major", responseUser.major)
                    SharedPreferenceUtil(context).setString("nickname", responseUser.nickname)
                    Log.d("유저", SharedPreferenceUtil(context).getUser().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                    _registerState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _registerState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    fun fetchUserInfoById(uId: Int) {
        viewModelScope.launch {
            _registerState.value = Resource.loading(null)
            try {
                val response = repository.getUserInfoById(uId)
                if (response.isSuccessful && response.body() != null) {
                    val responseUser = response.body()
                    _user.value = responseUser
                    if(responseUser!!.nickname == SharedPreferenceUtil(context).getUser().nickname) {
                        SharedPreferenceUtil(context).setUser(responseUser)
                        Log.d("유저", responseUser.toString())
                        Log.d(
                            "유저",
                            SharedPreferenceUtil(context).getString("nickname", "").toString()
                        )
                    }
                    _fetchUserState.value = Resource.success(responseUser)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                    _fetchUserState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _fetchUserState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    fun getUserByNickname(nickname: String): UserResponseModel? {
        var user: UserResponseModel? = null
        viewModelScope.launch {
            _registerState.value = Resource.loading(null)
            try {
                val response = repository.getUserByNickname(nickname)
                if (response.isSuccessful && response.body() != null) {
                    val responseUser = response.body()
                    _userByNickname.value = responseUser
                    _fetchUserState.value = Resource.success(responseUser)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                    _fetchUserState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _fetchUserState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
        return user
    }

    fun deleteUser() {
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)

        viewModelScope.launch {
            try {
                val response = repository.deleteUser(uId)
                if (response.isSuccessful && response.body() != null) {
                    _isDeleteSuccess.value = response.body()!!
                    Log.d("미란 유저 삭제", _isDeleteSuccess.value.toString())
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