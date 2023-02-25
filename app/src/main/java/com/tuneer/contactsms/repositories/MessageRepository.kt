package com.tuneer.contactsms.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.tuneer.contactsms.dataclass.Message
import com.tuneer.contactsms.helper.UtilFile
import com.tuneer.contactsms.networkdb.NetworkClient
import com.tuneer.contactsms.networkdb.UploadAPIs
import com.tuneer.contactsms.roomdb.AppDatabase
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Response

class MessageRepository(private val appDatabase: AppDatabase) {

    private val TAG = "MessageRepository"

    fun insertMultiContact(messageList: ArrayList<Message>) =
        appDatabase.getMessageDao().insert(messageList)

    fun insertContact(message: Message) = appDatabase.getMessageDao().insert(message)

    fun getMessageList() {

        var messageList: ArrayList<Message> = ArrayList()
        Log.d(
            "API",
            "-----API Start----\n" + "URL-- " + UtilFile.BASE_URL + UtilFile.GET_MESSAGE_JSONLIST + "\n" + "Method -- " + "QUERY GET" + "\n" + "----- End API----"
        )
        NetworkClient.getRetrofitClient(BaseURL = UtilFile.BASE_URL, "", null)
            ?.create(UploadAPIs::class.java)
            ?.queryparamsGetApiCall(UtilFile.GET_MESSAGE_JSONLIST)
            ?.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        if (response.code() == 200) {
                            val jsonObject = JSONTokener(
                                response.body()?.string() ?: ""
                            ).nextValue() as JSONObject
                            // Log.d(TAG, "onResponse: jsonObject: $jsonObject")
                            val jsonArray =
                                JSONTokener(jsonObject.getString("messages")).nextValue() as JSONArray
                            for (i in 0 until jsonArray.length()) {
                                //   Log.d(TAG, "onResponse: jsonArray: ${jsonArray.getJSONObject(i)}")
                                val message = Message(jsonArray.getJSONObject(i))
                                Log.d(TAG, "onResponse: message: " + Gson().toJson(message))
                                // insertContact(message)
                                messageList.add(message)
                            }
                            Log.d(
                                TAG,
                                "onResponse: MessageList found " + Gson().toJson(messageList)
                            )
                            if (messageList.size > 0) {
                                Log.d(TAG, "onResponse: MessageList found size" + messageList.size)
                                //  appDatabase.getCommonQueryDao().deleteMessageTable()
                                insertMultiContact(messageList)

                            }
                        } else {

                            if (response.body() != null) {
                                val jsonObject = JSONTokener(
                                    response.body()?.string() ?: ""
                                ).nextValue() as JSONObject
                                Log.d(TAG, "onResponse: else200 $jsonObject")
                            } else if (response.errorBody() != null) {
                                val jsonObject = JSONTokener(
                                    response.errorBody()?.string() ?: ""
                                ).nextValue() as JSONObject
                                Log.d(TAG, "onResponse: else200 $jsonObject")
                            }
                        }
                    } finally {
                        Log.d(TAG, "onResponse: finally")
                    }


                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")

                }
            })

    }

    fun getMessageListfromLocal(): LiveData<List<Message>> =
        appDatabase.getCommonQueryDao().allMessages()

    fun deleteMessages() = appDatabase.getCommonQueryDao().deleteMessageTable()

}