package com.example.xml_ver.network

//import com.example.xml_ver.data.retrofit.acceptation.AcceptationModel
//import com.example.xml_ver.data.retrofit.acceptation.AcceptationResponseModel
//import com.example.xml_ver.data.retrofit.comment.comment.CommentModel
//import com.example.xml_ver.data.retrofit.comment.comment.CommentResponseModel
//import com.example.xml_ver.data.retrofit.comment.reply.ReplyModel
//import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
//import com.example.xml_ver.data.retrofit.post.ClubPostModel
//import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
//import com.example.xml_ver.data.retrofit.post.HotPlacePostModel
//import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
//import com.example.xml_ver.data.retrofit.post.NoticeModel
//import com.example.xml_ver.data.retrofit.post.NoticeResponseModel
//import com.example.xml_ver.data.retrofit.post.PostModel
//import com.example.xml_ver.data.retrofit.post.PostResponseModel
//import com.example.xml_ver.data.retrofit.user.LoginModel
//import com.example.xml_ver.data.retrofit.user.MailModel
//import com.example.xml_ver.data.retrofit.user.UserModel
//import com.example.xml_ver.data.retrofit.user.UserResponseModel
//import com.example.xml_ver.data.retrofit.wish.WishListModel
import com.example.xml_ver.data.retrofit.acceptation.AcceptationModel
import com.example.xml_ver.data.retrofit.acceptation.AcceptationResponseModel
import com.example.xml_ver.data.retrofit.comment.comment.CommentModel
import com.example.xml_ver.data.retrofit.comment.comment.CommentResponseModel
import com.example.xml_ver.data.retrofit.comment.reply.ReplyModel
import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
import com.example.xml_ver.data.retrofit.post.ClubPostModel
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.data.retrofit.post.HotPlacePostModel
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.data.retrofit.post.NoticeModel
import com.example.xml_ver.data.retrofit.post.NoticeResponseModel
import com.example.xml_ver.data.retrofit.post.PostModel
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.data.retrofit.user.LoginModel
import com.example.xml_ver.data.retrofit.user.MailModel
import com.example.xml_ver.data.retrofit.user.UserModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.data.retrofit.wish.WishListModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // 게시물
    @POST("post")
    suspend fun registerPost(@Body post: PostModel): Response<PostResponseModel>

    @GET("post")
    suspend fun getPost(): Response<List<PostResponseModel>>

    @GET("post/{id}")
    suspend fun getOnePost(@Path("id") idx: Int): Response<PostResponseModel>

    @GET("post/postUser/{id}")
    suspend fun getPostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    @GET("post/24hours")
    suspend fun getTodayPost(): Response<List<PostResponseModel>>

    @DELETE("post/delete/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Boolean>

    // 유저

    @POST("login")
    suspend fun tryLogin(@Body loginModel: LoginModel): Response<Boolean>

    @POST("user")
    suspend fun registerUser(@Body user: UserModel): Response<UserResponseModel>

    @GET("user/find/{id}")
    suspend fun getUserInfoById(@Path("id") uId: Int): Response<UserResponseModel>

    @GET("user/{nickname}")
    suspend fun getUserByNickname(@Path("nickname") nickname: String): Response<UserResponseModel>

    @GET("user/find/student/{studentId}")
    suspend fun checkHaveAccount(@Path("studentId") studentId: Int): Response<Int>

    @DELETE("user/delete/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Boolean>

    @POST("mail")
    suspend fun sendMail(@Body mailModel: MailModel): Response<Boolean>

    // 소모임

    @POST("club")
    suspend fun registerClubPost(@Body post: ClubPostModel): Response<ClubPostResponseModel>

    @GET("club")
    suspend fun getClubPost(): Response<List<ClubPostResponseModel>>

    @GET("club/{id}")
    suspend fun getClubPostById(@Path("id") idx: Int): Response<ClubPostResponseModel>

    @GET("club/clubPostUser/{id}")
    suspend fun getClubPostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    @DELETE("club/delete/{id}")
    suspend fun deleteClubPost(@Path("id") id: Int): Response<Boolean>

    // 핫플레이스

    @POST("hot")
    suspend fun registerHotPlacePost(@Body post: HotPlacePostModel): Response<HotPlaceResponsePostModel>

    @GET("hot")
    suspend fun getHotPlacePost(): Response<List<HotPlaceResponsePostModel>>

    @GET("hot/{id}")
    suspend fun getHotPlacePostById(@Path("id") idx: Int): Response<HotPlaceResponsePostModel>

    @GET("hot/popular")
    suspend fun getPopularHotPlacePost(): Response<List<HotPlaceResponsePostModel>>

    @GET("hot/hotPlaceUser/{id}")
    suspend fun getHotPlacePostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    @DELETE("hot/delete/{id}")
    suspend fun deleteHotPlacePost(@Path("id") id: Int): Response<Boolean>

    // 모임 참여

    @GET("accept/{id}")
    suspend fun getJoinUserByPId(@Path("id") pId: Int): Response<List<AcceptationResponseModel>>

    @GET("accept/userList/{id}")
    suspend fun getAttendeeListByPId(@Path("id") pId: Int): Response<List<UserResponseModel>>

    // 댓글 가져오기
    @GET("postComment/commentPost/{id}")
    suspend fun getCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    @GET("clubComment/commentPost/{id}")
    suspend fun getClubCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    @GET("hotComment/commentPost/{id}")
    suspend fun getHotPlaceCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    // 댓글 작성 유저 가져오기
    @GET("postComment/commentUser/{id}")
    suspend fun getCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("clubComment/commentUser/{id}")
    suspend fun getClubCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("hotComment/commentUser/{id}")
    suspend fun getHotPlaceCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    // 댓글 작성
    @POST("postComment")
    suspend fun registerPostComment(@Body comment: CommentModel): Response<CommentResponseModel>

    @POST("clubComment")
    suspend fun registerClubComment(@Body comment: CommentModel): Response<CommentResponseModel>

    @POST("hotComment")
    suspend fun registerHotPlaceComment(@Body comment: CommentModel): Response<CommentResponseModel>

    // 대댓글 작성
    @POST("postReply")
    suspend fun registerPostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    @POST("clubReply")
    suspend fun registerClubPostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    @POST("hotReply")
    suspend fun registerHotPlacePostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    // 대댓글 가져오기
    @GET("postReply/find/{id}")
    suspend fun getReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    @GET("clubReply/find/{id}")
    suspend fun getClubReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    @GET("hotReply/find/{id}")
    suspend fun getHotPlaceReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    // 대댓글 작성 유저
    @GET("postReply/replyUser/{id}")
    suspend fun getReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("clubReply/replyUser/{id}")
    suspend fun getClubReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("hotReply/replyUser/{id}")
    suspend fun getHotPlaceReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>


    // wish

    @GET("wishMeeting/myList/{id}")
    suspend fun getWishMeetingPost(@Path("id") uId: Int): Response<List<PostResponseModel>>

    @GET("wishClub/myList/{id}")
    suspend fun getWishClubPost(@Path("id") uId: Int): Response<List<ClubPostResponseModel>>

    @GET("wish/myList/{id}")
    suspend fun getWishHotPlacePost(@Path("id") uId: Int): Response<List<HotPlaceResponsePostModel>>

    @POST("wishMeeting")
    suspend fun addWishMeetingPost(@Body wish: WishListModel): Response<PostResponseModel>

    @POST("wishClub")
    suspend fun addWishClubPost(@Body wish: WishListModel): Response<ClubPostResponseModel>

    @POST("wish")
    suspend fun addWishHotPlacePost(@Body wish: WishListModel): Response<HotPlaceResponsePostModel>

    @GET("wishMeeting/isExist/{uId}/{pId}")
    suspend fun checkIsWishedMeetingPost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @GET("wishClub/isExist/{uId}/{pId}")
    suspend fun checkIsWishedClubPost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @GET("wish/isExist/{uId}/{pId}")
    suspend fun checkIsWishedHotPlacePost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @DELETE("wishMeeting/delete/{uId}/{pId}")
    suspend fun deleteWishedMeetingPost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @DELETE("wishClub/delete/{uId}/{pId}")
    suspend fun deleteWishedClubPost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @DELETE("wish/delete/{uId}/{pId}")
    suspend fun deleteWishedHotPlacePost(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    // 이미지
    @Multipart
    @POST("image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<String>

    @Multipart
    @POST("image/list")
    suspend fun uploadImageList(@Part files: List<MultipartBody.Part>): Response<List<String>>

    // 공지사항
    @POST("notice")
    suspend fun registerNotice(@Body notice: NoticeModel): Response<NoticeResponseModel>

    @GET("notice")
    suspend fun getNotice(): Response<List<NoticeResponseModel>>

    @GET("notice/{id}")
    suspend fun getNoticeById(@Path("id") id: Int): Response<NoticeResponseModel>

    @GET("notice/visible/{id}")
    suspend fun changeNoticeVisibility(@Path("id") id: Int): Response<Boolean>

    @POST("accept")
    suspend fun registerAcceptRequest(@Body accept: AcceptationModel): Response<AcceptationResponseModel>

    @DELETE("accept/delete/{uId}/{pId}")
    suspend fun deleteAcceptRequest(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>

    @GET("accept/accept/{uId}/{pId}")
    suspend fun allowUserToJoin(
        @Path("uId") uId: Int,
        @Path("pId") pId: Int
    ): Response<Boolean>
}