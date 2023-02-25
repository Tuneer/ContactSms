package com.tuneer.contactsms.viewmodels

import androidx.lifecycle.ViewModel
import com.tuneer.contactsms.dataclass.Message
import com.tuneer.contactsms.repositories.MessageRepository


class MessageViewModel(private val messageRepository: MessageRepository) : ViewModel() {


    fun insertContactList(messageList: ArrayList<Message>) =
        messageRepository.insertMultiContact(messageList)

    fun getMessageList() = messageRepository.getMessageList()

    fun getMessageListfromLocal() = messageRepository.getMessageListfromLocal()
    fun deleteMessageList() = messageRepository.deleteMessages()

}