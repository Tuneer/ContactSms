package com.tuneer.contactsms.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.tuneer.contactsms.R
import com.tuneer.contactsms.databinding.ActivityComposeBinding
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.dataclass.Message
import com.tuneer.contactsms.helper.CustomDialog
import com.tuneer.contactsms.helper.UtilFile
import com.tuneer.contactsms.helper.commonlisteners.CommonYesNoDialogListener
import com.tuneer.contactsms.networkdb.NetworkClient
import com.tuneer.contactsms.networkdb.UploadAPIs
import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Response
import java.util.Random

class ComposeActivity : AppCompatActivity() {

    private val TAG = "ComposeActivity"
    lateinit var contact: Contact
    lateinit var binding: ActivityComposeBinding
    var commonDialog: CustomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        //old way of transfering data but not good fo large data
        /* if (intent != null) {
             contact = Gson().fromJson(intent.extras?.getString("Data"), Contact::class.java)

         }*/

        //NEW way of getting data from one activity to another without any issue.
        if (UtilFile.getDataTransferObject(baseContext) != null) {
            contact = UtilFile.getDataTransferObject(baseContext)!!
        }

        if (contact != null && contact.phoneNumber != null && !contact.phoneNumber.equals(
                "",
                ignoreCase = true
            )
        ) {
            //Generate Random OTP number of 6 digits
            binding.messagetobesent.setText("Hi. Your OTP is: " + randomSixDigitNumber())
            binding.senderName.text = "To : ${contact.firstName + " " + contact.lastName}"
        }

        binding.sendMessage.setOnClickListener {
            binding.messageStatus.visibility = View.INVISIBLE

            if (UtilFile.checkForInternet(this)) {
                //callAPI to sens the message  from Twillio API.
                sendMessage()
            } else {
                Toast.makeText(this, R.string.disconnected_msg, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun sendMessage() {
        var params =
            "${UtilFile.TWILIO_DEFAULT_PHONE} ${binding.messagetobesent.text.toString()} ${contact.countryCode}${contact.phoneNumber}"
        Log.d(
            "API",
            "-----API Start----\n" + "URL-- " + UtilFile.BASE_URL + UtilFile.GET_MESSAGE_JSONLIST + "\n" + "Method -- " + "FORM POST" + "\n" + "Parameters = \n" + params + "\n" + "----- End API----"
        )

        NetworkClient.getRetrofitClient(BaseURL = UtilFile.BASE_URL, "", null)
            ?.create(UploadAPIs::class.java)
            ?.formparamsPostApiCall(
                UtilFile.GET_MESSAGE_JSONLIST,
                UtilFile.TWILIO_DEFAULT_PHONE,
                binding.messagetobesent.text.toString(),
                "${contact.countryCode}${contact.phoneNumber}"
            )
            ?.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            val jsonObject = JSONTokener(
                                response.body()?.string() ?: ""
                            ).nextValue() as JSONObject
                            Log.d(TAG, "onResponse: success jsonObject: $jsonObject")
                            val message = Message(jsonObject)
                            Log.d(TAG, "onResponse: message: " + Gson().toJson(message))
                            if (message != null) {
                                binding.messageStatus.visibility = View.VISIBLE
                                binding.messageStatus.text = message.getStatus().toString()
                                getStatusforMessage(
                                    binding.messageStatus,
                                    message.getStatus().toString()
                                )
                            }

                        } else {
                            if (response.body() != null) {
                                val jsonObject = JSONTokener(
                                    response.body()?.string() ?: ""
                                ).nextValue() as JSONObject
                                Log.d(TAG, "onResponse: else200 $jsonObject")

                                openDialogMessage(jsonObject.getString("message"))

                            } else if (response.errorBody() != null) {
                                val jsonObject = JSONTokener(
                                    response.errorBody()?.string() ?: ""
                                ).nextValue() as JSONObject
                                Log.d(TAG, "onResponse: else200 $jsonObject")

                                openDialogMessage(jsonObject.getString("message"))
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

    private fun openDialogMessage(msg: String) {
        commonDialog = CustomDialog("Alert", msg, object : CommonYesNoDialogListener {
            override fun onYesCkickListener() {
                commonDialog?.dismiss()
            }

            override fun onNoClickedListener() {
                commonDialog?.dismiss()
            }
        })
        commonDialog!!.show(supportFragmentManager, "cmnMessage")

    }

    private fun randomSixDigitNumber(): String {

        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        Log.d(TAG, "randomSixDigitNumber: $rnd")
        val number: Int = rnd.nextInt(999999)
        Log.d(TAG, "randomSixDigitNumber: $number")
        //we can add some conditions here for 000000 digit number if occur.
        // this will convert any number sequence into 6 character.
        Log.d(TAG, "randomSixDigitNumber: ${String.format("%06d", number)}")
        return String.format("%06d", number)
    }

    private fun getStatusforMessage(statusMsg: TextView, status: String) {
        if (status.contentEquals("sent") || status.contentEquals("delivered")) {
            statusMsg.text = status
            statusMsg.setTextColor(Color.GREEN)
        } else if (status.contentEquals("pending") || status.contentEquals("failed")) {
            statusMsg.text = status
            statusMsg.setTextColor(Color.RED)
        } else if (status.contentEquals("queued")) {
            statusMsg.text = status
            statusMsg.setTextColor(Color.BLUE)
        }
    }
}