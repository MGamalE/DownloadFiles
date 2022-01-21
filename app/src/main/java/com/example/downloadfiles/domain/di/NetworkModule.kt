package com.example.downloadfiles.domain.di

import com.example.downloadfiles.domain.core.GatewayConst.BASE_URL
import com.example.downloadfiles.domain.gateway.FilesApi
import com.example.downloadfiles.domain.repository.FilesRepository
import com.example.downloadfiles.domain.repository.FilesRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideFilesApi(retrofit: Retrofit): FilesApi = retrofit.create(FilesApi::class.java)

    @Singleton
    @Provides
    fun providesFilesRepository(server: FilesApi): FilesRepository = FilesRepositoryImp(server)

}