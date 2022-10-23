package uz.androdev.memorization.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.data.db.AppDatabase
import uz.androdev.memorization.data.db.dao.FolderDao

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 2:54 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FoldersDaoModule {
    @Provides
    fun provideFoldersDao(appDatabase: AppDatabase): FolderDao {
        return appDatabase.folderDao
    }
}