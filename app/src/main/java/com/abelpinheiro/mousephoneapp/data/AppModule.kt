package com.abelpinheiro.mousephoneapp.data

import android.content.Context
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
    fun provideWebSocketDataSource(): WebSocketDataSource = WebSocketDataSource()

    @Provides
    @Singleton
    fun provideConnectionRepository(
        dataSource: WebSocketDataSource
    ): ConnectionRepository = ConnectionRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideGyroscopeManager(@ApplicationContext context: Context) : GyroscopeManager{
        return GyroscopeManager(context)
    }
}