package com.tuneer.contactsms.dataclass

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 12-02-2023.
 */
@Entity(tableName = "Contact")
data class Contact(var firstName:String,
                   var lastName:String,
                   @PrimaryKey
                   @NonNull
                   @ColumnInfo(name = "phoneNumber")
                   var phoneNumber:String,
                   var countryCode:String,
                   var description:String){


}
