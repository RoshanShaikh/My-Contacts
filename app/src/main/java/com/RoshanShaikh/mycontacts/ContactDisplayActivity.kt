package com.RoshanShaikh.mycontacts

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.RoshanShaikh.mycontacts.data.MyDBHandler
import com.RoshanShaikh.mycontacts.model.Contact
import kotlinx.android.synthetic.main.activity_contact_display.*

class ContactDisplayActivity : AppCompatActivity() {
    private lateinit var contact: Contact
    private var passingId: Boolean = false
    private var id: Int = 0
    private val db = MyDBHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_display)
        // getting the intent
        val intent = intent

        passingId = intent.getBooleanExtra("passingId", false)

        initActivity()

        save.setOnClickListener {
            saveContact()
        }

        numberTxt.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                numberTxt.clearFocus()
                hideKB(v)
                true
            } else
                false
        }

        //  starting dialerActivity on call button click
        call.setOnClickListener {
            startDialerActivity(this, numberTxt.text.toString())
        }
    }

    private fun initActivity() {

        /*  Checking if activity was started for editing existing contact or creating a new contact
          passingId = true means editing existing contact
          passingId = false means creating a new contact
          */

        //if passingId = true -> Editing existing contact
        if (passingId) {

            call.visibility = View.VISIBLE  // making the call button visible

            id = intent.getIntExtra("id", 0)    //  getting the id of contact from intentExtras

            //  setting text views to display existing contact detail

            val db = MyDBHandler(this)
            contact = db.getContact(id)
            nameTxt.setText(contact.name)
            numberTxt.setText(contact.phoneNumber)
        }

        //if passingId = false -> creating new contact
        else {

            call.visibility = View.GONE // making the call button

            // requesting focus for the name edit text and showing the keyboard
            nameTxt.requestFocus()
            showKB()

        }
        val handler = Handler()
        val run = object : Runnable {
            override fun run() {
                if (passingId) {
                    //  if contact's name name or number is changed then making the update button visible
                    if (contact.name != nameTxt.text.toString() || contact.phoneNumber != numberTxt.text.toString()) {
                        save.visibility = View.VISIBLE
                    } else {
                        save.visibility = View.GONE
                    }

                } else {
                    val phoneNumber = numberTxt.text.toString()

                    // if contact number is not Empty then showing the save button

                    if (phoneNumber.isNotEmpty()) {
                        save.visibility = View.VISIBLE
                    } else {
                        save.visibility = View.GONE
                    }
                }
                handler.postDelayed(this, 100)
            }
        }
        handler.post(run)
    }

    private fun saveContact() {

        //  updating the existing contact
        if (passingId) {
            val name: String = nameTxt.text.toString()
            val phoneNumber = numberTxt.text.toString()
            db.updateContact(Contact(id, name, phoneNumber))
            contact = db.getContact(id)
            nameTxt.setText(contact.name)
            numberTxt.setText(contact.phoneNumber)
            NavUtils.navigateUpFromSameTask(this)
        }

        // creating new contact if contact number is not empty
        else {
            var name: String = nameTxt.text.toString()
            val phoneNumber = numberTxt.text.toString()
            if (phoneNumber.isNotEmpty()) {
                if (name.isEmpty()) {
                    name = ""
                }
                db.createContact(Contact(id, name, phoneNumber))
                NavUtils.navigateUpFromSameTask(this)
            }
        }
    }

    private fun startDialerActivity(context: Context, number: String) {
        val u: Uri = Uri.parse("tel:$number")
        val intent = Intent(Intent.ACTION_DIAL, u)
        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            context.startActivity(intent)
        } catch (s: SecurityException) {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun showKB() {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun hideKB(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        val name: String = nameTxt.text.toString()
        val phoneNumber = numberTxt.text.toString()
        if (passingId) {
            if (name != contact.name || phoneNumber != contact.phoneNumber) {
                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setPositiveButton(
                            "Save"
                        ) { _, _ ->
                            saveContact()
                        }
                        setNegativeButton(
                            "Discard"
                        ) { _, _ ->
                            NavUtils.navigateUpFromSameTask(this@ContactDisplayActivity)
                        }
                        setTitle("Do You Want to Update the Contact?")
                        setCancelable(true)
                    }
                    builder.create()
                }
                alertDialog!!.show()
            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_contact_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                if (passingId) {
                    val name: String = nameTxt.text.toString()
                    val phoneNumber = numberTxt.text.toString()
                    db.updateContact(Contact(id, name, phoneNumber))
                } else {
                    val name: String = nameTxt.text.toString()
                    val phoneNumber = numberTxt.text.toString()
                    if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                        db.createContact(Contact(id, name, phoneNumber))
                        NavUtils.navigateUpFromSameTask(this);
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

*/

}