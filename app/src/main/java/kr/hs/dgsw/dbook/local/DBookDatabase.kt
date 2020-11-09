package kr.hs.dgsw.dbook.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.hs.dgsw.dbook.local.dao.BookDao
import kr.hs.dgsw.dbook.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class DBookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}