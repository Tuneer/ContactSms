package com.tuneer.contactsms.roomdb

import androidx.room.Dao
import com.tuneer.contactsms.dataclass.Contact

@Dao
abstract class ContactDao : BaseDao<Contact>() {
}