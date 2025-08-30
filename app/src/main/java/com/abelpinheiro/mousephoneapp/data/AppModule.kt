package com.abelpinheiro.mousephoneapp.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Provides
    @Singleton
    fun provideWebSocketDataSource(): WebSocketDataSource = WebSocketDataSource()

    @Provides
    @Singleton
    fun provideConnectionRepository(
        dataSource: WebSocketDataSource
    ): ConnectionRepository = ConnectionRepositoryImpl(dataSource)
}