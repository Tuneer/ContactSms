package com.tuneer.contactsms.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuneer.contactsms.R
import com.tuneer.contactsms.adapters.CustomMessageAdapter
import com.tuneer.contactsms.databinding.FragmentSecondBinding
import com.tuneer.contactsms.dataclass.Contact
import com.tuneer.contactsms.dataclass.Message
import com.tuneer.contactsms.helper.UtilFile
import com.tuneer.contactsms.repositories.ContactRepository
import com.tuneer.contactsms.repositories.MessageRepository
import com.tuneer.contactsms.roomdb.AppDatabase
import com.tuneer.contactsms.viewmodels.ContactViewModel
import com.tuneer.contactsms.viewmodels.ContactViewModelFactory
import com.tuneer.contactsms.viewmodels.MessageViewModel
import com.tuneer.contactsms.viewmodels.MessageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val TAG = "SecondFragment"
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var messageList: ArrayList<Message>
    private lateinit var appDatabase: AppDatabase
    private lateinit var messageRepository: MessageRepository
    private lateinit var contactRepository: ContactRepository
    private lateinit var factory: MessageViewModelFactory
    private lateinit var contactFactory: ContactViewModelFactory
    private lateinit var viewModel: MessageViewModel
    private lateinit var viewModelContact: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        binding.recyclerviewMessage.layoutManager = LinearLayoutManager(context)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialization of messagelist.
        messageList = ArrayList()

        appDatabase = AppDatabase(requireContext())
        messageRepository = MessageRepository(appDatabase)
        contactRepository = ContactRepository(appDatabase)
        factory = MessageViewModelFactory(messageRepository)
        contactFactory = ContactViewModelFactory(contactRepository)
        viewModel = ViewModelProvider(this, factory)[MessageViewModel::class.java]
        viewModelContact = ViewModelProvider(this, contactFactory)[ContactViewModel::class.java]

        binding.buttonSecond.setOnClickListener {

            if (UtilFile.checkForInternet(requireContext())) {
                //callAPI to get the message List from Twillio API.
                getMessageList()
            } else {
                Toast.makeText(context, R.string.disconnected_msg, Toast.LENGTH_SHORT).show()
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getMessageListfromLocal().observe(viewLifecycleOwner) {
                Log.d(TAG, "onViewCreated: observer inside " + it.size)
                run {
                    if (it.isNotEmpty()) {
                        binding.noMessageFound.visibility = View.GONE
                        Log.d(TAG, "onViewCreated: suspend started: ")
                        Log.d(TAG, "onViewCreated: inside suspend")
                        getFilterMessageList(
                            it,
                            appDatabase.getCommonQueryDao().getContacts()
                        )

                        if (messageList.size > 0) {
                            // This will pass the ArrayList to our Adapter
                            val adapter = CustomMessageAdapter(
                                messageList,
                                requireContext(),
                                object : CustomMessageAdapter.ItemClickListener {
                                    override fun OnItemClick(position: Int, message: Message) {

                                    }

                                })
                            binding.noMessageFound.visibility = View.GONE
                            // Setting the Adapter with the recyclerview
                            binding.recyclerviewMessage.adapter = adapter

                        } else {
                            binding.noMessageFound.visibility = View.VISIBLE
                        }
                    } else {
                        binding.noMessageFound.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun getFilterMessageList(messages: List<Message>?, contacts: List<Contact>) {
        messageList = ArrayList()
        var message: Message? = null
        Log.d(
            TAG,
            "getFilterMessageList: messages " + messages?.size + "  contacts: " + contacts.size
        )

        if (contacts != null) {
            for (cnt in contacts) {
                if (messages != null) {
                    for (msg in messages) {
                        Log.d(TAG, "getFilterMessageList: ${cnt.countryCode}${cnt.phoneNumber}")
                        if ("${cnt.countryCode}${cnt.phoneNumber}".contains(msg.getTo())) {
                            Log.d(
                                TAG,
                                "getFilterMessageList: ${cnt.countryCode}${cnt.phoneNumber} equals to ${msg.getTo()}"
                            )
                            message = Message(
                                msg,
                                cnt.firstName + " " + cnt.lastName,
                                cnt.firstName,
                                cnt.lastName
                            )
                            Log.d(
                                TAG,
                                "getFilterMessageList: messageobj  $message"
                            )
                            if (message != null) {
                                messageList.add(message)
                            }
                        }
                    }
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()

        if (UtilFile.checkForInternet(requireContext())) {
            //callAPI to get the message List from Twillio API.
            getMessageList()
        } else {
            Toast.makeText(context, R.string.disconnected_msg, Toast.LENGTH_SHORT).show()
            binding.noMessageFound.visibility = View.VISIBLE
        }
    }

    private fun getMessageList() {

        viewModel.deleteMessageList()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getMessageList()

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}