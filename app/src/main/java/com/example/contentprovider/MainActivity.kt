package com.example.contentprovider

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.core.content.PackageManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            getPhoneContacts()
        }
    }

    private fun getPhoneContacts() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                0
            )
            return // Return early if permission is not granted
        }

        val contentResolver: ContentResolver = contentResolver
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        Log.d("Soumya", "Total contacts ${cursor?.count.toString()}")

        cursor?.use {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val contactNameIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val contactNumberIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    val contactName =
                        if (contactNameIndex != -1) it.getString(contactNameIndex) else ""
                    val contactNumber =
                        if (contactNumberIndex != -1) it.getString(contactNumberIndex) else ""

                    Log.d("Soumya", "$contactName and $contactNumber")
                }
            }
        }
    }

}