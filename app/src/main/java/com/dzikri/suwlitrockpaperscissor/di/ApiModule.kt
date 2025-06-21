package com.dzikri.suwlitrockpaperscissor.di

import com.dzikri.suwlitrockpaperscissor.data.network.RetrofitInstance
import com.dzikri.suwlitrockpaperscissor.data.network.UserApiInterface
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {

    @Provides
    fun provideUserApiClass(): UserApiInterface {
        return RetrofitInstance.getInstance().create<UserApiInterface>(UserApiInterface::class.java)
    }
}