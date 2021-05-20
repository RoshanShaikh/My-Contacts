package com.RoshanShaikh.mycontacts.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.RoshanShaikh.mycontacts.model.Contact
import com.RoshanShaikh.mycontacts.params.Params


class MyDBHandler(context: Context) :
    SQLiteOpenHelper(context, Params.DB_NAME, null, Params.DB_VERSION) {
    private val logTag = "MyDBHandler"
    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery =
            "CREATE TABLE ${Params.TABLE_NAME}(${Params.KEY_ID} INTEGER PRIMARY KEY, " +
                    "${Params.KEY_NAME} TEXT, ${Params.KEY_PHONE} TEXT)"

        Log.d(logTag, "Query being run is : $createQuery")
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun createContact(contact: Contact) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Params.KEY_NAME, contact.name)
        values.put(Params.KEY_PHONE, contact.phoneNumber)

        db.insert(Params.TABLE_NAME, null, values)
        db.close()

        Log.d(logTag, "Inserted ${contact.phoneNumber} as ${contact.name}")
    }

    fun getAllContacts(): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()

        val db = this.readableDatabase
        val selectionQuery = "SELECT * FROM ${Params.TABLE_NAME}"
        val cursor = db.rawQuery(selectionQuery, null)

        if (cursor.moveToFirst()) {
            do {
                contactList.add(
                    Contact(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Params.KEY_NAME, contact.name)
        values.put(Params.KEY_PHONE, contact.phoneNumber)

        //Lets update now
        return db.update(
            Params.TABLE_NAME,
            values,
            Params.KEY_ID + "=?",
            arrayOf(java.lang.String.valueOf(contact.id))
        )
    }

    fun updateContact(
        contact: Contact,
        parameterToChange: String,
        isNameGivenAsParameterToChange: Boolean
    ): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Params.KEY_NAME, contact.name)
        values.put(Params.KEY_PHONE, contact.phoneNumber)

        return db.update(
            Params.TABLE_NAME,
            values, (
                    if (isNameGivenAsParameterToChange)
                        Params.KEY_NAME
                    else
                        Params.KEY_PHONE)
                    + "=?",
            arrayOf(java.lang.String.valueOf(parameterToChange))
        )
    }

    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(Params.TABLE_NAME, Params.KEY_ID + "=?", arrayOf(java.lang.String.valueOf(id)))
        db.close()
    }

    fun getCount(): Int {
        val query = "SELECT * FROM ${Params.TABLE_NAME}"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun getContact(id: Int): Contact {
        val db = readableDatabase
        val query = "SELECT * FROM ${Params.TABLE_NAME} WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val name = cursor.getString(1)
        val number = cursor.getString(2)
        val cId = cursor.getInt(0)
        cursor.close()
        return Contact(cId, name, number)
    }
}