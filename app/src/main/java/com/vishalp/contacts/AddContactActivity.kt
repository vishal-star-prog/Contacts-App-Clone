package com.vishalp.contacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vishalp.contacts.dao.ContactDao
import com.vishalp.contacts.model.Contact

class AddContactActivity : AppCompatActivity() {

    private lateinit var first: EditText
    private lateinit var last: EditText
    private lateinit var number: EditText
    private lateinit var add: Button
    private lateinit var contactDao: ContactDao
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        first = findViewById(R.id.first)
        last = findViewById(R.id.last)
        number = findViewById(R.id.number)
        add = findViewById(R.id.add)
        contactDao = ContactDao()
        auth = Firebase.auth

        add.setOnClickListener {
            val firstName = first.text.toString()
            val lastName = last.text.toString()
            val phoneNumber = number.text.toString()

            if(firstName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val currentUserId = auth.currentUser!!.uid
                val contact = Contact(firstName,lastName,phoneNumber,currentUserId)
                contactDao.addContact(contact)
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}