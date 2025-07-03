package com.dzikri.suwlitrockpaperscissor.di

import com.dzikri.suwlitrockpaperscissor.data.network.GameApiInterface
import com.dzikri.suwlitrockpaperscissor.data.network.UserApiInterface
import com.dzikri.suwlitrockpaperscissor.data.network.WebSocketInstance
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideUserApiClass(retrofit: Retrofit): UserApiInterface {
        return retrofit.create<UserApiInterface>(UserApiInterface::class.java)
    }

    @Provides
    fun provideGameApiClass(retrofit: Retrofit): GameApiInterface {
        return retrofit.create<GameApiInterface>(GameApiInterface::class.java)
    }

    @Provides
    fun provideUserWebSocketClass(): WebSocketInstance {
        return WebSocketInstance()
    }
}