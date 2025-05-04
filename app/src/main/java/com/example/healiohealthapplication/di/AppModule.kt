package com.example.healthapplication.di

import android.content.Context
import com.example.healthapplication.utils.StepCounter
import com.example.healthapplication.utils.StepPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStepPrefs(@ApplicationContext context: Context): StepPrefs {
        return StepPrefs(context)
    }

    @Provides
    @Singleton
    fun provideStepCounter(
        @ApplicationContext context: Context,
        stepPrefs: StepPrefs,
        stepPermissions: Permissions
    ): StepCounter {
        return StepCounter(context, stepPrefs, stepPermissions)
    }
}