package com.tuneer.contactsms.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tuneer.contactsms.dataclass.Contact
import jahirfiquitiva.libs.textdrawable.TextDrawable
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 11-02-2023.
 */

sealed class UtilFile {
    companion object {
        private const val contact_FILENAME: String = "contact.txt"
        private const val TAG = "UtilFile"

        fun getDrawableFromName(context: Context, FN: String, LN: String, imageView: ImageView) {
            var strFI = ""
            var strLI = ""
            if (FN != "") {
                strFI = FN.get(0).toString() + ""
            }
            if (LN != "") {
                strLI = LN.get(0).toString() + ""
            }

            val drawable2: TextDrawable = TextDrawable.builder()
                .buildRound(
                    strFI.uppercase(Locale.getDefault()) + "" + strLI.uppercase(Locale.getDefault()),
                    ResourcesCompat.getColor(
                        context.getResources(),
                        com.google.android.material.R.color.design_dark_default_color_primary_variant,
                        null
                    )
                )
            imageView.setImageDrawable(drawable2)
        }

        const val GET_MESSAGE_JSONLIST = "Messages.json"

        //  const val USERNAME: String = "gdkntuneer@gmail.com"
        const val TWILIO_ACCOUNT_SID = "ACe23e856d41f08b52752ffab038c6bfd0"

        //  const val TWILIO_AUTH_TOKEN ="7fe8ebc39903f003c2c74733fbd87587"
        //  const val TWILIO_SERVICE_ID ="ISdde10109e7df030e9451af41f64d8e54"
        const val TWILIO_DEFAULT_PHONE = "+13858326297"
        const val AUTH_TOKEN =
            "QUNlMjNlODU2ZDQxZjA4YjUyNzUyZmZhYjAzOGM2YmZkMDo3ZmU4ZWJjMzk5MDNmMDAzYzJjNzQ3MzNmYmQ4NzU4Nw=="
        const val BASE_URL = "https://api.twilio.com/2010-04-01/Accounts/$TWILIO_ACCOUNT_SID/"

        const val FRAG_CONTACT_ID = 0
        const val FRAG_MESSAGE_ID = 2


        //DATE FUNCTIONS
        fun formatDateToString(
            date: Date?, format: String?,
            timeZone: String?
        ): String? {
// null check
            var timeZone = timeZone
            if (date == null) return null
            // create SimpleDateFormat object with input format
            val sdf = SimpleDateFormat(format)
            // default system timezone if passed null or empty
            if (timeZone == null || "".equals(timeZone.trim { it <= ' ' }, ignoreCase = true)) {
                timeZone = Calendar.getInstance().timeZone.id
            }
            // set timezone to SimpleDateFormat
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            // return Date in required format with timezone as String
            return sdf.format(date)
        }


        fun formatStringToDate(
            datestring: String?, format: String?,
            timeZone: String?
        ): Date? {
// null check
            var timeZone = timeZone
            if (datestring == null) return null
            // create SimpleDateFormat object with input format
            val sdf = SimpleDateFormat(format)
            // default system timezone if passed null or empty
            if (timeZone == null || "".equals(timeZone.trim { it <= ' ' }, ignoreCase = true)) {
                timeZone = Calendar.getInstance().timeZone.id
            }
            // set timezone to SimpleDateFormat
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            var date: Date? = null
            try {
                date = sdf.parse(datestring)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

// return Date in required format with timezone as String
            return date
        }


        private fun writeToFile(data: String, context: Context, fileName: String) {
            try {
                val outputStreamWriter =
                    OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
                outputStreamWriter.write(data)
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e(TAG, " exception File write failed: $e")
            }
        }

        private fun readFromFile(context: Context, fileName: String): String? {
            var ret = ""
            try {
                val inputStream: InputStream? = context.openFileInput(fileName)
                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String? = ""
                    val stringBuilder = StringBuilder()
                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        stringBuilder.append("\n").append(receiveString)
                    }
                    inputStream.close()
                    if (!stringBuilder.toString().trim { it <= ' ' }
                            .replace("\"\"", "").equals("", ignoreCase = true)) {
                        ret = stringBuilder.toString()
                    }
                }
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "File not found: $e")
            } catch (e: IOException) {
                Log.e(TAG, "Can not read file: $e")
            }
            return ret
        }

        fun saveDataTransferObject(contact: Contact, contextNew: Context) {

            val gson = Gson()
            if (contact == null) {
                writeToFile(
                    gson.toJson(""),
                    contextNew,
                    contact_FILENAME
                )
            } else {
                writeToFile(
                    gson.toJson(contact),
                    contextNew,
                    contact_FILENAME
                )
            }
        }

        fun getDataTransferObject(contextNew: Context): Contact? {
            var igUser: Contact? = null
            val gson = Gson()
            if (readFromFile(contextNew, contact_FILENAME)!!.trim { it <= ' ' }
                    .equals("", ignoreCase = true)) {
                return igUser
            } else {
                igUser = gson.fromJson(
                    readFromFile(contextNew, contact_FILENAME),
                    object : TypeToken<Contact?>() {}.type
                )
            }
            return igUser
        }


        fun checkForInternet(context: Context): Boolean {

            // register activity with the connectivity manager service
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // if the android version is equal to M
            // or greater we need to use the
            // NetworkCapabilities to check what type of
            // network has the internet connection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Returns a Network object corresponding to
                // the currently active default data network.
                val network = connectivityManager.activeNetwork ?: return false

                // Representation of the capabilities of an active network.
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(network) ?: return false

                return when {
                    // Indicates this network uses a Wi-Fi transport,
                    // or WiFi has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                    // Indicates this network uses a Cellular transport. or
                    // Cellular has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                    // else return false
                    else -> false
                }
            } else {
                // if the android version is below M
                @Suppress("DEPRECATION") val networkInfo =
                    connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }


    }


}