package com.tuneer.contactsms.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 13-02-2023.
 */
@Entity(tableName = "Message")
class Message(
    @PrimaryKey(autoGenerate = true)
    private var sno: Int = 0,
    private var numsegments: String = "",
    private var body: String = "",
    private var direction: String = "",
    @ColumnInfo(name = "from")
    private var from: String = "",
    @ColumnInfo(name = "dateupdated")
    private var dateupdated: String = "",
    @ColumnInfo(name = "errormessage")
    private var errormessage: String = "",
    @ColumnInfo(name = "to")
    private var to: String = "",
    @ColumnInfo(name = "datecreated")
    private var datecreated: String = "",
    @ColumnInfo(name = "status")
    private var status: String = "",
    @ColumnInfo(name = "datesent")
    private var datesent: String = ""
) {

    var name: String = ""
    var fn: String = ""
    var ln: String = ""


    constructor(jsonObject: JSONObject) : this(0, "", "", "", "", "", "", "", "", "") {
        if (jsonObject.has("num_segments")) {
            numsegments = jsonObject.getInt("num_segments").toString()
        }
        if (jsonObject.has("body")) {
            body = jsonObject.getString("body")
        }
        if (jsonObject.has("direction")) {
            direction = jsonObject.getString("direction")
        }
        if (jsonObject.has("from")) {
            from = jsonObject.getString("from")
        }
        if (jsonObject.has("date_updated")) {
            dateupdated = jsonObject.getString("date_updated")
        }
        if (jsonObject.has("error_message")) {
            errormessage = jsonObject.getString("error_message")
        }
        if (jsonObject.has("to")) {
            to = jsonObject.getString("to")
        }
        if (jsonObject.has("date_created")) {
            datecreated = jsonObject.getString("date_created")
        }
        if (jsonObject.has("status")) {
            status = jsonObject.getString("status")
        }
        if (jsonObject.has("date_sent")) {
            datesent = jsonObject.getString("date_sent")
        }
    }

    constructor(message: Message, name: String, fn: String, ln: String) : this() {
        if (message != null) {
            this.body = message.body
            this.numsegments = message.numsegments
            this.direction = message.direction
            this.to = message.to
            this.from = message.from
            this.name = name
            this.fn = fn
            this.ln = ln
            this.status = message.status
            this.datesent = message.datesent
            this.datecreated = message.datecreated
            this.dateupdated = message.dateupdated
            this.errormessage = message.errormessage
        }
    }

    fun getSno(): Int {
        return sno
    }

    fun setSno(sno: Int) {
        this.sno = sno
    }

    fun getBody(): String {
        return body
    }

    fun setBody(body: String) {
        this.body = body
    }

    fun getNumsegments(): String {
        return numsegments
    }

    fun setNumsegments(numsegments: String) {
        this.numsegments = numsegments
    }

    fun getDirection(): String {
        return direction
    }

    fun setDirection(direction: String) {
        this.direction = direction
    }

    fun getFrom(): String {
        return from
    }

    fun setFrom(from: String) {
        this.from = from
    }

    fun getTo(): String {
        return to
    }

    fun setTo(to: String) {
        this.to = to
    }

    fun getStatus(): String {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getErrormessage(): String {
        return errormessage
    }

    fun setErrormessage(error_message: String) {
        this.errormessage = error_message
    }

    fun getDateupdated(): String {
        return dateupdated
    }

    fun setDateupdated(date_updated: String) {
        this.dateupdated = date_updated
    }

    fun getDatecreated(): String {
        return datecreated
    }

    fun setDatecreated(date_created: String) {
        this.datecreated = date_created
    }

    fun getDatesent(): String {
        return datesent
    }

    fun setDatesent(date_sent: String) {
        this.datesent = date_sent
    }

    fun setContactName(name: String) {
        this.name = name
    }

    fun getContactName(): String {
        return name
    }
}


