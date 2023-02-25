package com.tuneer.contactsms.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuneer.contactsms.repositories.MessageRepository

class MessageViewModelFactory(private val messageRepository: MessageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    private val TAG = "ContactViewModelFactory"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(MessageRepository::class.java)
            return constructor.newInstance(messageRepository)
        } catch (e: Exception) {
            Log.e(TAG, "create: constructor " + e.message.toString())
        }
        return super.create(modelClass)
    }
}