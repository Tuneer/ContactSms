package com.tuneer.contactsms.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tuneer.contactsms.R
import com.tuneer.contactsms.activities.DescriptionActivity
import com.tuneer.contactsms.adapters.CustomAdapter
import com.tuneer.contactsms.databinding.FragmentFirstBinding
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.helper.CustomDialog
import com.tuneer.contactsms.helper.UtilFile
import com.tuneer.contactsms.helper.commonlisteners.CommonYesNoDialogListener
import com.tuneer.contactsms.repositories.ContactRepository
import com.tuneer.contactsms.roomdb.AppDatabase
import com.tuneer.contactsms.viewmodels.ContactViewModel
import com.tuneer.contactsms.viewmodels.ContactViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    public val TAG = "FirstFragment"
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var contactList: ArrayList<Contact>
    var commonDialog: CustomDialog? = null
    private lateinit var appDatabase: AppDatabase
    private lateinit var contactRepository: ContactRepository
    private lateinit var factory: ContactViewModelFactory
    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.recyclerviewContact.layoutManager = LinearLayoutManager(context)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //late initialization of list so it can be ready to fill data otherwise compilation error will come.
        contactList = ArrayList()

        appDatabase = AppDatabase(requireContext())
        contactRepository = ContactRepository(appDatabase)
        factory = ContactViewModelFactory(contactRepository)
        viewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]


        binding.buttonFirst.setOnClickListener {
            /* //add temporary contact at runtime for testing.
             commonDialog = CustomDialog(
                 "Alert",
                 "Please verify phone number with the callerID from server side. \n Contact will be added temporarily to send SMS after verification. but you will be able to see sent status in message list tab.",
                 object : CommonYesNoDialogListener {
                     override fun onYesCkickListener() {
                         commonDialog?.dismiss()
                         if (contactList.isNotEmpty()) {

                         } else {
                             contactList.plus(Contact("", "", "", "", ""))
                         }
                     }

                     override fun onNoClickedListener() {
                         commonDialog?.dismiss()
                     }
                 })
             commonDialog!!.show(parentFragmentManager, "commonDialog")*/

            //New Code
            commonDialog = CustomDialog(
                "Alert",
                "This functionality is not available until further notice. Please contact developer for this functionalities status.",
                object : CommonYesNoDialogListener {
                    override fun onYesCkickListener() {
                        commonDialog?.dismiss()
                        Snackbar.make(
                            view,
                            "Contact developer at mahatpuretuneer@gmail.com",
                            Snackbar.LENGTH_LONG
                        )
                            .setAnchorView(R.id.viewPager)
                            .setAction("Action", null).show()
                    }

                    override fun onNoClickedListener() {
                        commonDialog?.dismiss()
                    }
                })
            commonDialog!!.show(parentFragmentManager, "commonDialog")


        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getContactList().observe(viewLifecycleOwner) {
                run {
                    if (it.isNotEmpty()) {
                        // This will pass the ArrayList to our Adapter
                        val adapter = CustomAdapter(
                            it,
                            requireContext(),
                            object : CustomAdapter.ItemClickListener {
                                override fun OnItemClick(position: Int, contact: Contact) {
                                    //Need to change Intent put extra method in future if contact dataclass size increased
                                    // because intent transfer can't handle large data. for now we can use intent data transfer.
                                    if (contact != null) {
                                        UtilFile.saveDataTransferObject(contact, context!!)
                                        startActivity(
                                            Intent(
                                                context,
                                                DescriptionActivity::class.java
                                            )
                                        )

                                    }
                                }

                            })

                        // Setting the Adapter with the recyclerview
                        binding.recyclerviewContact.adapter = adapter
                        binding.noContactFound.visibility = View.GONE
                    } else {
                        binding.noContactFound.visibility = View.VISIBLE
                    }

                }
            }

        }


    }

    override fun onResume() {
        super.onResume()

        //getting a contact list from .json assets folder.
        getContactList()
    }

    //getting contact list from json file stored in assets folder
    // or it can be get from anywhere like database,server etc. then parsing will accordingly
    private fun getContactList() {
        //this method is getting the values from assets folder.
        contactList = getDataFromJsonAndSave(requireActivity())!!
        Log.d(TAG, "getContactList: " + Gson().toJson(contactList))
        if (contactList.isNotEmpty()) {
            Log.d(TAG, "getContactList: " + contactList.size)
            //CoroutineScope for adding data in viewmodel.
            CoroutineScope(Dispatchers.IO).launch {
                Log.d(TAG, "getContactList: launch CoroutineScope: ")
                viewModel.insertContactList(contactList)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun getDataFromJsonAndSave(baseContext: Context): ArrayList<Contact>? {
    val jsonLocation: String = assetJSONFile("cc.json", baseContext)
    Log.d(TAG, "getDataFromJsonAndSave: " + jsonLocation.length)
    if (jsonLocation.isNotEmpty()) {
        Log.d(TAG, "JSON have some issues check json file. : ")
    }
    val type = object : TypeToken<List<Contact>>() {}.type
    return Gson().fromJson<ArrayList<Contact>?>(jsonLocation, type)
}

fun assetJSONFile(filename: String, context: Context): String {
    try {
        val manager = context.assets
        val file = manager.open(filename)
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    } catch (e: Exception) {
        Log.e("assetJSONFile()", e.message + "")
    }
    return ""
}
