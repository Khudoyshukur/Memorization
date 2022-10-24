package uz.androdev.memorization.data.db

import android.content.Context
import androidx.room.*
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.entity.FolderEntity

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:21 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Database(
    entities = [
        FolderEntity::class,
        FlashCardEntity::class
    ],
    exportSchema = true,
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val folderDao: FolderDao
    abstract val flashCardDao: FlashCardDao

    companion object {
        private const val DATABASE_NAME = "quran.db"

        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}