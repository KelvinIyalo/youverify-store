package com.kelviniyalo.youverifystorekelviniyalo.di

import android.content.Context
import androidx.room.Room
import com.kelviniyalo.youverifystorekelviniyalo.BuildConfig
import com.kelviniyalo.youverifystorekelviniyalo.common.NetworkClient
import com.kelviniyalo.youverifystorekelviniyalo.data.local_data.DatabaseDao
import com.kelviniyalo.youverifystorekelviniyalo.data.local_data.ProductDatabase
import com.kelviniyalo.youverifystorekelviniyalo.data.provider.AppConfigImpl
import com.kelviniyalo.youverifystorekelviniyalo.data.remoteData.ProductsApiService
import com.kelviniyalo.youverifystorekelviniyalo.data.repository.DatabaseRepositoryImpl
import com.kelviniyalo.youverifystorekelviniyalo.data.repository.ProductsRepositoryImpl
import com.kelviniyalo.youverifystorekelviniyalo.data.user_auth.UserAuthRepositoryImpl
import com.kelviniyalo.youverifystorekelviniyalo.domain.provider.AppConfig
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.DatabaseRepository
import com.kelviniyalo.youverifystorekelviniyalo.domain.repository.ProductsRepository
import com.kelviniyalo.youverifystorekelviniyalo.domain.user_auth.UserAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YouVerifyAppModule {

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context) = context


    @Singleton
    @Provides
    fun provideAppConfig() = AppConfigImpl() as AppConfig

    @Provides
    fun provideMoviesApiModule(
        networkClient: NetworkClient
    ): ProductsApiService {
        return networkClient.getApiService(BuildConfig.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ProductsApiService) =
        ProductsRepositoryImpl(apiService) as ProductsRepository

    @Singleton
    @Provides
    fun providesDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ProductDatabase::class.java, "product_entity_db").build()

    @Singleton
    @Provides
    fun provideProductEntityDao(database: ProductDatabase) = database.databaseDao()

    @Provides
    @Singleton
    fun provideDatabaseRepo(databaseDao: DatabaseDao) =
        DatabaseRepositoryImpl(databaseDao) as DatabaseRepository

    @Provides
    @Singleton
    fun provideUserAuthRepo() =
        UserAuthRepositoryImpl() as UserAuthRepository
}