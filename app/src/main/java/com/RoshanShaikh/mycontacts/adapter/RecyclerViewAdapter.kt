package com.RoshanShaikh.mycontacts.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.RoshanShaikh.mycontacts.ContactDisplayActivity
import com.RoshanShaikh.mycontacts.R
import com.RoshanShaikh.mycontacts.model.Contact
import kotlinx.android.synthetic.main.contact_row.view.*


class RecyclerViewAdapter(val context: Context, val contactList: List<Contact>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val logTag = "RecyclerViewAdapter"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        val contact = contactList[position]
        holder.itemView.name.text = contact.name
        holder.itemView.number.text = contact.phoneNumber
    }

    override fun getItemCount(): Int {
        return contactList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnCreateContextMenuListener {

        init {
            itemView.setOnClickListener(this)
            itemView.image.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this);
            itemView.call.setOnClickListener {
                startDialerActivity(context,itemView.number.text.toString())
                val u: Uri = Uri.parse("tel:" + itemView.number.text.toString())
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
        }

        override fun onClick(v: View?) {
            val intent = Intent(context, ContactDisplayActivity::class.java)
            val contact = contactList[adapterPosition]
            Log.d(logTag, contact.id.toString())
            intent.putExtra("id", contact.id)
            intent.putExtra("passingId", true)
            context.startActivity(intent)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(
                contactList[this.adapterPosition].id,
                1222,
                this.adapterPosition,
                R.string.delete_contact
            )
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
}