package com.tuneer.contactsms.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tuneer.contactsms.R
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.helper.UtilFile
import kotlinx.coroutines.NonDisposableHandle.parent

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 12-02-2023.
 */
class CustomAdapter(private val mList: List<Contact>, val context:Context,listener:ItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val TAG = "CustomAdapter"
    var clickListener:ItemClickListener?=listener

    interface ItemClickListener{
        fun OnItemClick(position:Int,contact: Contact)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inflator_card_contact_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contact = mList[position]
        Log.d(TAG, "onBindViewHolder: "+Gson().toJson(contact))
        if (contact!= null) {

            // sets the image to the imageview from our itemHolder class
            UtilFile.getDrawableFromName(context, contact.firstName, contact.lastName, holder.imageView)

            // sets the text to the textview from our itemHolder class
            holder.textView.text = contact.firstName + " " + contact.lastName
            holder.itemView.setOnClickListener(View.OnClickListener{
                clickListener?.OnItemClick(position,contact)
            })
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pic)
        val textView: TextView = itemView.findViewById(R.id.name)
    }
}