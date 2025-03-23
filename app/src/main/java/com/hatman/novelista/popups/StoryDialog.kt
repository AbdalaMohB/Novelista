package com.hatman.novelista.popups

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.hatman.novelista.R
import com.hatman.novelista.fileUtils.FileManager

class StoryDialog(val onPositive: (String) -> Unit) : DialogFragment() {
   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.

            val view=inflater.inflate(R.layout.name_select, null)
            builder.setView(view)
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->

                        val name= view.findViewById<EditText>(R.id.storyName)
                        //Log.d("HELLO", "onCreateDialog: ${name?.text.toString()}")
                        onPositive(name?.text.toString())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()!!.cancel()
                    })
            //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_Theme)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        //setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        super.onStart()
    }
}