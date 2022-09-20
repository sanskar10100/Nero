package dev.sanskar.nero.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sanskar.nero.BuildConfig
import dev.sanskar.nero.network.GoogleBooksService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(OkHttpClient.Builder().callTimeout(100, TimeUnit.SECONDS).build())
            .build()
    }

    @Singleton
    @Provides
    fun provideBackendService(retrofit: Retrofit): GoogleBooksService = retrofit.create(GoogleBooksService::class.java)

//    @Singleton
//    @Provides
//    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
//        context,
//        PhotoPlayDB::class.java,
//        "photoplay_db"
//    ).fallbackToDestructiveMigration().build()
}