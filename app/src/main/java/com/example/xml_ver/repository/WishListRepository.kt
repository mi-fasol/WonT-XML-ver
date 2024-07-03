package com.example.xml_ver.repository

import android.util.Log
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.data.retrofit.wish.WishListModel
import com.example.xml_ver.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class WishListRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getWishMeetingPost(uId: Int): Response<List<PostResponseModel>> {
        return retrofitClient.service.getWishMeetingPost(uId)
    }

    suspend fun getWishClubPost(uId: Int): Response<List<ClubPostResponseModel>> {
        return retrofitClient.service.getWishClubPost(uId)
    }

    suspend fun getWishHotPlacePost(uId: Int): Response<List<HotPlaceResponsePostModel>> {
        return retrofitClient.service.getWishHotPlacePost(uId)
    }

    suspend fun addWishPost(wish: WishListModel) : Response<PostResponseModel>{
        return retrofitClient.service.addWishMeetingPost(wish)
    }

    suspend fun addWishClub(wish: WishListModel) : Response<ClubPostResponseModel>{
        return retrofitClient.service.addWishClubPost(wish)
    }

    suspend fun addWishPlace(wish: WishListModel) : Response<HotPlaceResponsePostModel>{
        return retrofitClient.service.addWishHotPlacePost(wish)
    }

    suspend fun checkIsWishedMeetingPost(uId: Int, pId: Int, type: Int) : Response<Boolean>{
        val api = when(type){
            1 -> retrofitClient.service.checkIsWishedMeetingPost(uId, pId)
            2 -> retrofitClient.service.checkIsWishedClubPost(uId, pId)
            else -> retrofitClient.service.checkIsWishedHotPlacePost(uId, pId)
        }
        return api
    }

    suspend fun deleteWishList(uId: Int, pId: Int, type: Int) : Response<Boolean> {
        val api = when(type){
            1 -> retrofitClient.service.deleteWishedMeetingPost(uId, pId)
            2 -> retrofitClient.service.deleteWishedClubPost(uId, pId)
            else -> retrofitClient.service.deleteWishedHotPlacePost(uId, pId)
        }
        return api
    }
}