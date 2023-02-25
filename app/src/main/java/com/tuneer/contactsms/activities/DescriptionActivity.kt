package com.tuneer.contactsms.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tuneer.contactsms.databinding.ActivityDescriptionBinding
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.helper.UtilFile

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 13-02-2023.
 */
class DescriptionActivity : AppCompatActivity() {

    lateinit var contact: Contact
    lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
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

        if (contact != null) {
            // sets the image to the imageview from our binding class
            UtilFile.getDrawableFromName(
                baseContext,
                contact.firstName,
                contact.lastName,
                binding.userPic
            )
            binding.fullName.text = "Contact Name : " + contact.firstName + " " + contact.lastName
            binding.phoneNumber.text =
                "Contact Number : " + contact.countryCode + " " + contact.phoneNumber
        }

        binding.sendMessage.setOnClickListener {
            if (contact != null) {
                startActivity(Intent(this@DescriptionActivity, ComposeActivity::class.java))
            }
        }
    }
}