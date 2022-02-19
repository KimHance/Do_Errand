package com.example.toyproject_housework.FCM

import com.example.toyproject_housework.FCM.Constants.Companion.FCM_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FCM_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : FcmInterface by lazy {
        retrofit.create(FcmInterface::class.java)
    }

    // Client
    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    // 헤더 추가
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "key=AAAAKFLo_8w:APA91bEPatbWl4AnXi6UR7ts6ffBzEXlbSyVTsAR46A0n0lX7PhIFkBZmaJoPvxpKuM6DDniDUufqD0yEeWoRM6T7vQ2EBwGSiFqaYryfT9_rKA2H9c1vUf3Ojva8pYA09oDdvXAoOdO")
                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    }
}
