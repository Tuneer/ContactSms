package com.tuneer.contactsms.repositories

import androidx.lifecycle.LiveData
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.roomdb.AppDatabase


class ContactRepository(private val appDatabase: AppDatabase) {

    fun insertMultiContact(contactList: ArrayList<Contact>) = appDatabase.getContactDao().insert(contactList)
    fun getAllContacts(): LiveData<List<Contact>> = appDatabase.getCommonQueryDao().allContacts()

}