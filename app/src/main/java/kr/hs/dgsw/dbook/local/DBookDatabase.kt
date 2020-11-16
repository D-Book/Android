package kr.hs.dgsw.dbook.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.hs.dgsw.dbook.local.dao.BookDao
import kr.hs.dgsw.dbook.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class DBookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: DBookDatabase? = null

        fun getDatabase(context: Context): DBookDatabase? {
            if (INSTANCE == null) {
                synchronized(DBookDatabase::class.java) {

                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                DBookDatabase::class.java, "DBookDatabase"
                        )
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}