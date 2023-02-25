package com.tuneer.contactsms.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuneer.contactsms.repositories.ContactRepository

class ContactViewModelFactory(private val contactRepository: ContactRepository) : ViewModelProvider.NewInstanceFactory() {

    private val TAG = "ContactViewModelFactory"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(ContactRepository::class.java)
            return constructor.newInstance(contactRepository)
        } catch (e: Exception) {
            Log.e(TAG, "create: constructor "+e.message.toString())
        }
        return super.create(modelClass)
    }
}