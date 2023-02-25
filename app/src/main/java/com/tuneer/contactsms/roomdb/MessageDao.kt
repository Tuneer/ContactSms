package com.tuneer.contactsms.roomdb

import androidx.room.Dao
import com.tuneer.contactsms.dataclass.Message

@Dao
abstract class MessageDao: BaseDao<Message>() {
}