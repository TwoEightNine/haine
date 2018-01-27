package global.msnthrp.messenger.network

import global.msnthrp.messenger.dialogs.DialogResponse
import global.msnthrp.messenger.dialogs.Message
import global.msnthrp.messenger.login.LoginResponse
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.network.model.BaseResponse
import global.msnthrp.messenger.profile.User
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by msnthrp on 22/01/18.
 */
interface ApiService {

    @FormUrlEncoded
    @POST("/register")
    fun register(@Field("name") name: String,
                 @Field("password") password: String): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("name") name: String,
              @Field("password") password: String): Single<BaseResponse<LoginResponse>>

    @GET("/messages")
    fun getDialogs(): Single<BaseResponse<DialogResponse>>

    @GET("/user/search")
    fun search(@Query("q") query: String): Single<BaseResponse<List<User>>>

    @FormUrlEncoded
    @POST("/message/send")
    fun sendMessage(@Field("body") text: String,
                    @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @GET("/messages/{userId}")
    fun getMessages(@Path("userId") userId: Int): Single<BaseResponse<List<Message>>>

    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: Int): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/user/photo")
    fun updatePhoto(@Field("photo") photo: String): Single<BaseResponse<Int>>

    @GET("/stickers")
    fun getStickers(): Single<BaseResponse<List<Sticker>>>

    @FormUrlEncoded
    @POST("/message/sticker")
    fun sendSticker(@Field("sticker_id") stickerId: Int,
                    @Field("to_id") toId: Int): Single<BaseResponse<Int>>
}