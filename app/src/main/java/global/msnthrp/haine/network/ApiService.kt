package global.msnthrp.haine.network

import global.msnthrp.haine.dialogs.DialogResponse
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.login.LoginResponse
import global.msnthrp.haine.model.Sticker
import global.msnthrp.haine.network.model.BaseResponse
import global.msnthrp.haine.model.User
import global.msnthrp.haine.network.model.PollResponse
import global.msnthrp.haine.network.model.UploadResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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

    @GET("/auth.terminate")
    fun terminateSessions(): Single<BaseResponse<Int>>

    @GET("/messages.getDialogs")
    fun getDialogs(): Single<BaseResponse<DialogResponse>>

    @GET("/user.search")
    fun search(@Query("q") query: String): Single<BaseResponse<List<User>>>

    @FormUrlEncoded
    @POST("/messages.sendText")
    fun sendMessage(@Field("text") text: String,
                    @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/messages.sendFile")
    fun sendAttachments(@Field("attached") attached: String,
                        @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @GET("/messages.get/{userId}")
    fun getMessages(@Path("userId") userId: Int): Single<BaseResponse<ArrayList<Message>>>

    @GET("/user.get/{userId}")
    fun getUser(@Path("userId") userId: Int): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/user.uploadPhoto")
    fun uploadPhoto(@Field("avatar") photo: String): Single<BaseResponse<Int>>

    @GET("/stickers.get")
    fun getStickers(): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/messages.sendSticker")
    fun sendSticker(@Field("sticker_id") stickerId: Int,
                    @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @GET("/messages.poll")
    fun poll(@Query("next_message_from") nextMessageFrom: Int,
             @Query("next_xchg_from") nextXchgFrom: Long): Single<BaseResponse<PollResponse>>

    @FormUrlEncoded
    @POST("/exchange.commit")
    fun commitExchange(@Field("p") p: String,
                       @Field("g") g: String,
                       @Field("public") public: String,
                       @Field("to_id") toId: Int): Single<BaseResponse<Int>>

    @GET("/exchange.safePrime")
    fun getPrime(): Single<BaseResponse<String>>

    @Multipart
    @POST("https://file.io")
    fun uploadFile(@Part file: MultipartBody.Part): Single<UploadResponse>

    @GET
    fun downloadFile(@Url link: String): Single<ResponseBody>
}