package com.tuneer.contactsms.networkdb

import android.content.Context
import com.tuneer.contactsms.helper.UtilFile
import com.tuneer.contactsms.helper.UtilFile.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 13-02-2023.
 */

object NetworkClient {

    private var retrofit: Retrofit? = null


    fun getRetrofitClient(BaseURL: String?, token: String?, context: Context?): Retrofit? {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().connectTimeout(200, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                    .readTimeout(2, TimeUnit.MINUTES)
                    .addInterceptor(Interceptor { chain ->
                        val original: Request = chain.request()
                        val builder = original.newBuilder()
                        builder.header(
                            "Content-Type",
                            "text/plain"
                        )
                        builder.header(
                            "Authorization",
                            "Basic " + UtilFile.AUTH_TOKEN

                        )
                        val request = builder.build()
                        chain.proceed(request)
                    }) //.addInterceptor(loggingInterceptor)
                    .build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }


}