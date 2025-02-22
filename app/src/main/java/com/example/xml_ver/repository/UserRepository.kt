package com.example.xml_ver.repository

import android.util.Log
import com.example.xml_ver.data.retrofit.user.LoginModel
import com.example.xml_ver.data.retrofit.user.MailModel
import com.example.xml_ver.data.retrofit.user.UserModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun tryLogin(loginModel: LoginModel): Response<Boolean> {
        return retrofitClient.service.tryLogin(loginModel)
    }

    suspend fun registerUser(user: UserModel): Response<UserResponseModel> {
        return retrofitClient.service.registerUser(user)
    }

    suspend fun getUserInfoById(uId: Int) :Response<UserResponseModel>{
        return retrofitClient.service.getUserInfoById(uId)
    }

    suspend fun getUserByNickname(nickname:String) :Response<UserResponseModel>{
        return retrofitClient.service.getUserByNickname(nickname)
    }

    suspend fun checkHaveAccount(studentId: Int): Response<Int> {
        Log.d("미란 checkHaveAccount", "실행")
        return retrofitClient.service.checkHaveAccount(studentId)
    }

    suspend fun deleteUser(uId: Int): Response<Boolean> {
        return retrofitClient.service.deleteUser(uId)
    }

    suspend fun sendMail(mailModel: MailModel) : Response<Boolean>{
        return retrofitClient.service.sendMail(mailModel)
    }
}