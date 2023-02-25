package com.tuneer.contactsms.networkdb

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 13-02-2023.
 */

interface UploadAPIs {

    @GET
    fun queryparamsGetApiCall(@Url apiName: String?): Call<ResponseBody>

    @POST
    fun queryparamsPostApiCall(@Url apiName: String?): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun formparamsPostApiCall(@Url apiName:String?, @Field("From") From:String?,@Field("Body") Body:String?,@Field("To") To:String?):Call<ResponseBody>?
}