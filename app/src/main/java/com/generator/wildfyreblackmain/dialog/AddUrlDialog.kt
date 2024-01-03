package com.generator.wildfyreblackmain.dialog
import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.generator.wildfyreblackmain.events.AddUrlEvent
import com.generator.wildfyreblackmain.R
import org.greenrobot.eventbus.EventBus

class AddUrlDialog {

    fun show(activity: Activity) {
        var dialog = Dialog(activity,)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_add_url)

        var add = dialog.findViewById<Button>(R.id.SaveUrlBtn)
        var cancel = dialog.findViewById<Button>(R.id.cancelBtn)
        var editText = dialog.findViewById<EditText>(R.id.addUrlEditTxt)

        add.setOnClickListener {
            EventBus.getDefault().post(AddUrlEvent(editText.text.toString()))
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}