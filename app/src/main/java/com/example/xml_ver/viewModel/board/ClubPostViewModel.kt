package com.example.xml_ver.viewModel.board

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.data.retrofit.post.ClubPostModel
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.repository.ImageRepository
import com.example.xml_ver.repository.PostRepository
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class ClubPostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val imageRepository: ImageRepository,
    private val context: Context
) : ViewModel() {

    private val _clubPostList = MutableStateFlow<List<ClubPostResponseModel>>(emptyList())
    val clubPostList: StateFlow<List<ClubPostResponseModel>> = _clubPostList.asStateFlow()

    private val _clubPost = MutableStateFlow<ClubPostResponseModel?>(null)
    val clubPost: StateFlow<ClubPostResponseModel?> = _clubPost.asStateFlow()

    private val _user = MutableStateFlow<UserResponseModel?>(null)
    val user: StateFlow<UserResponseModel?> = _user.asStateFlow()

    private val _clubPostState =
        MutableStateFlow<Resource<ClubPostResponseModel>>(Resource.loading(null))
    val clubPostState: StateFlow<Resource<ClubPostResponseModel>> = _clubPostState.asStateFlow()

    private val _clubPostListState =
        MutableStateFlow<Resource<List<ClubPostResponseModel>>>(Resource.loading(null))
    val clubPostListState: StateFlow<Resource<List<ClubPostResponseModel>>> =
        _clubPostListState.asStateFlow()

    private val _clubPostRegisterState =
        MutableStateFlow<Resource<ClubPostResponseModel>>(Resource.loading(null))
    val clubPostRegisterState: StateFlow<Resource<ClubPostResponseModel>> =
        _clubPostRegisterState.asStateFlow()

    private val _clubPostDeleteState =
        MutableStateFlow<Boolean?>(null)
    val clubPostDeleteState: StateFlow<Boolean?> =
        _clubPostDeleteState.asStateFlow()

    // 유효성 검사

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val image = MutableStateFlow("")
    val person = MutableStateFlow(0)
    val content = MutableStateFlow("")

    var isValid: StateFlow<Boolean> =
        combine(
            title,
            person,
            description,
            image,
            content
        ) { title, person, description, image, content ->
            title.isNotBlank() && person != 0 && description.isNotBlank() && image.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val result = imageRepository.uploadImage(imageUri)
                image.value = result
                Log.d("사진", image.value)
            } catch (e: HttpException) {
                Log.e("ImageUploadViewModel", "API Error: HTTP ${e.code()} ${e.message}")
            } catch (e: Exception) {
                Log.e("ImageUploadViewModel", "Unknown Error: ${e.message}")
            }
        }
    }

    init {
        viewModelScope.launch {
            getClubPostList()
        }
    }

    suspend fun getClubPostList() {
        viewModelScope.launch {
            _clubPostListState.value = Resource.loading(null)
            try {
                val response = repository.getClubPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _clubPostList.value = postList!!
                    _clubPostListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _clubPostListState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _clubPostListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getOneClubPost(idx: Int) {
        viewModelScope.launch {
            _clubPostState.value = Resource.loading(null)
            try {
                val response = repository.getOneClubPost(idx)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _clubPost.value = post!!
                    _clubPostState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _clubPostState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _clubPostState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getClubPostingUser(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getClubPostingUser(pId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()
                    _user.value = user!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun registerPost() {
        val today = getCurrentDateTime()

        val _image = if (image.value == "") null else image.value

        val post = ClubPostModel(
            title.value,
            content.value,
            SharedPreferenceUtil(context).getString("nickname", "")!!.toString(),
            person.value,
            description.value,
            today,
            _image,
            0
        )

        viewModelScope.launch {
            _clubPostRegisterState.value = Resource.loading(null)
            try {
                val response = repository.registerClubPost(post)
                if (response.isSuccessful && response.body() != null) {
                    _clubPostRegisterState.value = Resource.success(response.body())
                    Log.d("게시물 전송", response.body().toString())
                    resetAll()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    private fun resetAll() {
        title.value = ""
        description.value = ""
        image.value = ""
        person.value = 0
        content.value = ""
        _clubPostRegisterState.value = Resource.loading(null)
    }

    fun deletePost(pId: Int) {
        viewModelScope.launch {
            _clubPostDeleteState.value = null
            try {
                val response = repository.deleteClubPost(pId)
                if (response.isSuccessful && response.body() != null) {
                    _clubPostDeleteState.value = response.body()
                    Log.d("게시물 전송", response.body().toString())
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