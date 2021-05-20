package com.RoshanShaikh.mycontacts

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.RoshanShaikh.mycontacts.adapter.RecyclerViewAdapter
import com.RoshanShaikh.mycontacts.data.MyDBHandler
import com.RoshanShaikh.mycontacts.model.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"
    private val myDBHandler = MyDBHandler(this)
    private lateinit var contactArrayList: ArrayList<Contact>
    private lateinit var recyclerAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactRecyclerView.setHasFixedSize(true)
        contactRecyclerView.layoutManager = LinearLayoutManager(this)

        contactArrayList = myDBHandler.getAllContacts()
        recyclerAdapter = RecyclerViewAdapter(this, contactArrayList.sortedBy { it.name })
        recyclerAdapter.notifyDataSetChanged()
        contactRecyclerView.adapter = recyclerAdapter

        registerForContextMenu(contactRecyclerView);
        add_contact.setOnClickListener {
            intent.putExtra("passingId", false)
            val intent = Intent(this, ContactDisplayActivity::class.java)
            this.startActivity(intent)
        }
    }

    @Override
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1222 -> {
                myDBHandler.deleteContact(item.groupId)
                contactArrayList.clear()
                contactArrayList = myDBHandler.getAllContacts()
                recyclerAdapter = RecyclerViewAdapter(this, contactArrayList)
                contactRecyclerView.adapter = recyclerAdapter
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onResume() {
        contactArrayList.clear()
        contactArrayList = myDBHandler.getAllContacts()
        recyclerAdapter = RecyclerViewAdapter(this, contactArrayList)
        contactRecyclerView.adapter = recyclerAdapter
        super.onResume()
    }
}