package com.tuneer.contactsms.viewmodels

import androidx.lifecycle.ViewModel
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.repositories.ContactRepository

class ContactViewModel(private val contactRepository: ContactRepository) :ViewModel() {

    fun insertContactList(contactList: ArrayList<Contact>) = contactRepository.insertMultiContact(contactList)
    fun getContactList() = contactRepository.getAllContacts()

}