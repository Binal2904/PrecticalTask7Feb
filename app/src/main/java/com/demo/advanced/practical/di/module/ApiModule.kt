package com.demo.advanced.practical.di.module

import com.demo.advanced.practical.BuildConfig
import com.demo.advanced.practical.di.qualifier.ApiHttpClient
import com.demo.advanced.practical.di.qualifier.ApiHttpLogger
import com.demo.advanced.practical.di.qualifier.ApiRetrofit
import com.demo.advanced.practical.di.qualifier.ApiUrl
import com.demo.advanced.practical.di.qualifier.ApiUrlGson
import com.demo.advanced.practical.di.qualifier.BaseApiService
import com.demo.advanced.practical.restfullapi.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @ApiUrl
    @Singleton
    @Provides
    fun provideApiUrl(): String = BuildConfig.BASE_URL


    @Provides
    @Singleton
    @ApiHttpLogger
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val mInterceptor = HttpLoggingInterceptor()
        mInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return mInterceptor
    }


    @Provides
    @Singleton
    @ApiHttpClient
    fun provideOkHttpClient(@ApiHttpLogger loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val mConnectionTimeOut = 7000L
        return if (BuildConfig.DEBUG) OkHttpClient.Builder()
            .readTimeout(mConnectionTimeOut, TimeUnit.MILLISECONDS)
            .connectTimeout(mConnectionTimeOut, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor).build()
        else OkHttpClient.Builder().readTimeout(mConnectionTimeOut, TimeUnit.MILLISECONDS)
            .connectTimeout(mConnectionTimeOut, TimeUnit.MILLISECONDS).build()

    }


    @Provides
    @Singleton
    @ApiUrlGson
    fun setGson() = GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    @ApiRetrofit
    fun provideRetrofit(
        @ApiHttpClient okHttpClient: OkHttpClient, @ApiUrlGson gson: Gson, @ApiUrl baseUrl: String
    ): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(baseUrl)
            .client(okHttpClient).build()


    @Provides
    @Singleton
    @BaseApiService
    fun provideApiService(@ApiRetrofit retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


}