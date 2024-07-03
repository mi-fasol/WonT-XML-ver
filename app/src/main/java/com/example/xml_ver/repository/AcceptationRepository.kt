package com.example.xml_ver.repository

import com.example.xml_ver.data.retrofit.acceptation.AcceptationModel
import com.example.xml_ver.data.retrofit.acceptation.AcceptationResponseModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class AcceptationRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getJoinUserByPId(pId: Int): Response<List<AcceptationResponseModel>> {
        return retrofitClient.service.getJoinUserByPId(pId)
    }

    suspend fun getAttendeeListByPId(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getAttendeeListByPId(pId)
    }

    suspend fun registerAcceptRequest(accept: AcceptationModel): Response<AcceptationResponseModel> {
        return retrofitClient.service.registerAcceptRequest(accept)
    }

    suspend fun deleteAcceptRequest(uId: Int, pId: Int): Response<Boolean> {
        return retrofitClient.service.deleteAcceptRequest(uId, pId)
    }

    suspend fun allowUserToJoin(uId: Int, pId: Int): Response<Boolean> {
        return retrofitClient.service.allowUserToJoin(uId, pId)
    }
}