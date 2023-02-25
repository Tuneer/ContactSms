package com.tuneer.contactsms.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tuneer.contactsms.R
import com.tuneer.contactsms.dataclass.Message
import com.tuneer.contactsms.helper.UtilFile
import java.util.Date

class CustomMessageAdapter(
    private val mList: List<Message>,
    val context: Context,
    listener: ItemClickListener
) : RecyclerView.Adapter<CustomMessageAdapter.ViewHolder>() {

    private val TAG = "CustomAdapter"
    var clickListener: ItemClickListener? = listener

    interface ItemClickListener {
        fun OnItemClick(position: Int, message: Message)
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val pic: ImageView = itemView.findViewById(R.id.pic)
        val name: TextView = itemView.findViewById(R.id.name)
        val message: TextView = itemView.findViewById(R.id.message)
        val status_msg: TextView = itemView.findViewById(R.id.status_msg)
        val dateSent: TextView = itemView.findViewById(R.id.dateSent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflator_card_message_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = mList[position]
        Log.d(TAG, "onBindViewHolder: " + Gson().toJson(message))
        if (message != null) {

            // sets the image to the imageview from our itemHolder class
            UtilFile.getDrawableFromName(
                context,
                message.fn,
                message.ln,
                holder.pic
            )

            // sets the text to the textview from our itemHolder class
            holder.name.text = message.name
            holder.message.text = message.getBody()
            //set status of message in color
            getStatusforMessage(holder.status_msg, message.getStatus())

            var date: Date? = UtilFile.formatStringToDate(
                message.getDatesent(),
                "E, d MMM yyyy HH:mm:ss Z",
                "UTC"
            )

            Log.d(TAG, "onBindViewHolder: Date: $date")

            var dateString: String? =
                UtilFile.formatDateToString(date, "E, d MMM yyyy HH:mm:ss a", "")
            holder.dateSent.text = "Sent at: $dateString"
            //holder.dateSent.text = message.getDatesent()

            holder.itemView.setOnClickListener(View.OnClickListener {
                clickListener?.OnItemClick(position, message)
            })
        }
    }

    private fun getStatusforMessage(statusMsg: TextView, status: String) {
        if (status.contentEquals("sent") || status.contentEquals("delivered")) {
            statusMsg.text = status
            statusMsg.setTextColor(Color.GREEN)
        } else if (status.contentEquals("pending") || status.contentEquals("failed")) {
            statusMsg.text = status
            statusMsg.setTextColor(Color.RED)
        }
    }
}