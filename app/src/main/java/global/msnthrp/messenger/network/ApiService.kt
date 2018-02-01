package global.msnthrp.messenger.network

import global.msnthrp.messenger.dialogs.DialogResponse
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.login.LoginResponse
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.network.model.BaseResponse
import global.msnthrp.messenger.model.User
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by msnthrp on 22/01/18.
 */
interface ApiService {

    @FormUrlEncoded
    @POST("/auth.signUp")
    fun register(@Field("name") name: String,
                 @Field("password") password: String): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/auth.logIn")
    fun login(@Field("name") name: String,
              @Field("password") password: String): Single<BaseResponse<LoginResponse>>

    @GET("/messages.getDialogs")
    fun getDialogs(): Single<BaseResponse<DialogResponse>>

    @GET("/user.search")
    fun search(@Query("q") query: String): Single<BaseResponse<List<User>>>

    @FormUrlEncoded
    @POST("/messages.send")
    fun sendMessage(@Field("text") text: String,
                    @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @GET("/messages.get/{userId}")
    fun getMessages(@Path("userId") userId: Int): Single<BaseResponse<ArrayList<Message>>>

    @GET("/user.get/{userId}")
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

    @GET("/poll")
    fun poll(@Query("next_from") nextFrom: Int): Single<BaseResponse<List<Message>>>
}