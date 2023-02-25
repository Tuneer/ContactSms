package com.tuneer.contactsms.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tuneer.contactsms.R
import com.tuneer.contactsms.helper.commonlisteners.CommonDialogListener
import com.tuneer.contactsms.helper.commonlisteners.CommonYesNoDialogListener

/**
 * Company Name GDKN
 * Created by Tuneer Mahatpure on 13-02-2023.
 */
class CustomDialog(header: String, body: String, listener: CommonYesNoDialogListener) : DialogFragment() {


    private var yesNoDialogListener: CommonYesNoDialogListener? = listener
    private var header: String?=header
    private var body: String?=body
    private var tv_dialog_header: TextView? = null
    private var tv_dialog_message: TextView? = null
    private var btn_dialog_ok: Button? = null
    private var btn_dialog_cancel: Button? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        return inflater.inflate(R.layout.dialog_common_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv_dialog_header = dialog?.findViewById<View>(R.id.tv_dialog_header) as TextView
        tv_dialog_message =  dialog?.findViewById<View>(R.id.tv_dialog_message) as TextView
        btn_dialog_ok =  dialog?.findViewById<View>(R.id.btn_dialog_ok) as Button
        btn_dialog_cancel =  dialog?.findViewById<View>(R.id.btn_dialog_cancel) as Button
        if (header != null) {
            tv_dialog_header!!.text = header
        }
        if (body != null) {
            tv_dialog_message!!.text = body
        }

        btn_dialog_ok!!.text = "Yes"
        btn_dialog_cancel!!.text = "No"
        btn_dialog_ok!!.setOnClickListener { yesNoDialogListener!!.onYesCkickListener() }
        btn_dialog_cancel!!.setOnClickListener { yesNoDialogListener!!.onNoClickedListener() }

    }

}