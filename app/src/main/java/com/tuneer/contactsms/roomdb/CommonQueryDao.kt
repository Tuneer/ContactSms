package com.tuneer.contactsms.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.dataclass.Message

/**
 * Created by - Tuneer
 */
@Dao
interface CommonQueryDao {
    @Query("SELECT COUNT(*) FROM Contact")
    fun contactsCount(): Int

    @Query("SELECT * FROM Contact")
    fun allContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM Contact")
    fun getContacts(): List<Contact>

    // Deleting all existing Tables
    @Query("DELETE FROM Contact")
    fun deleteContactTable()

    // Deleting all existing Tables
    @Query("DELETE FROM Message")
    fun deleteMessageTable()

    @Query("SELECT * FROM Message ORDER BY 'datesent' ASC")
    fun allMessages(): LiveData<List<Message>>

}