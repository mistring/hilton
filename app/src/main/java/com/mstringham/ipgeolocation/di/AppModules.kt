package com.mstringham.ipgeolocation.di

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mstringham.ipgeolocation.data.api.IpApi
import com.mstringham.ipgeolocation.data.api.IpApiRepo
import com.mstringham.ipgeolocation.data.api.NetworkIpApiRepo
import com.mstringham.ipgeolocation.data.db.GeolocationDao
import com.mstringham.ipgeolocation.data.db.GeolocationDatabase
import com.mstringham.ipgeolocation.data.db.GeolocationDatabase.Companion.DATABASE_NAME
import com.mstringham.ipgeolocation.data.db.GeolocationRepo
import com.mstringham.ipgeolocation.data.db.GeolocationRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    private const val API_BASE_NAME = "ApiBaseNameForUrl"
    private const val API_BASE_URL = "http://ip-api.com/"

    @Provides
    @Singleton
    @Named(API_BASE_NAME)
    fun provideApiBaseUrl() = API_BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val isDebuggable = 0 != appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        return if (isDebuggable) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient
                .Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder().build()
        }
    }

    @Provides
    @Singleton
    fun provideIpApi(
        @Named(API_BASE_NAME) baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): IpApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IpApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): GeolocationDatabase {
        return Room.databaseBuilder(
            appContext,
            GeolocationDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideIpDbEntityDao(db: GeolocationDatabase): GeolocationDao {
        return db.geolocationDao()
    }

}

@Module
@InstallIn(ViewModelComponent::class)
object RepoModule {

    @Provides
    @ViewModelScoped
    fun provideGeolocationRepo(itemDao: GeolocationDao): GeolocationRepo = GeolocationRepoImpl(itemDao)

    @Provides
    @ViewModelScoped
    fun provideIpApiRepo(ipApi: IpApi): IpApiRepo = NetworkIpApiRepo(ipApi)

}
