package com.tuneer.contactsms.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.dataclass.Message

@Database(
    entities = [Contact::class, Message::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getContactDao(): ContactDao
    abstract fun getMessageDao(): MessageDao
    abstract fun getCommonQueryDao(): CommonQueryDao

    companion object {

        private const val DB_NAME = "db_contact.db"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).allowMainThreadQueries().build()

    }

}