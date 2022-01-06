package com.vishalp.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.vishalp.contacts.dao.ContactDao
import com.vishalp.contacts.model.Contact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity(), IRVAdaptor {

    private lateinit var addContact: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactDao: ContactDao
    private lateinit var auth: FirebaseAuth
    private lateinit var adaptor: RVAdaptor
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addContact = findViewById(R.id.create)
        recyclerView = findViewById(R.id.recyclerView)
        contactDao = ContactDao()
        auth = Firebase.auth

        addContact.setOnClickListener {
            val intent = Intent(this,AddContactActivity::class.java)
            startActivity(intent)
        }

        checkPermissions()

        setUpRecyclerView()

    }

    private fun checkPermissions() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),10101)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = null
        val contactCollection = contactDao.contactCollection
        val currentUserId = auth.currentUser!!.uid
        val query = contactCollection.whereEqualTo("userId",currentUserId).orderBy("firstName",Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Contact>().setQuery(query,Contact::class.java).build()
        adaptor = RVAdaptor(recyclerViewOption,this)
        recyclerView.adapter = adaptor
    }

    override fun onStart() {
        super.onStart()
        adaptor.startListening()
    }

    override fun onStop() {
        super.onStop()
        adaptor.stopListening()
    }

    override fun onCallClicked(contactId: String) {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            GlobalScope.launch {
                val contact = contactDao.getContactById(contactId).await().toObject(Contact::class.java)!!
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:${contact.phoneNumber}")
                startActivity(callIntent)
            }
        }
    }

    override fun onMessageClicked(contactId: String) {
        GlobalScope.launch {
            Log.d(TAG, "onMessageClicked: Message intent working properly")
            val contact = contactDao.getContactById(contactId).await().toObject(Contact::class.java)!!
            val messageIntent = Intent(Intent.ACTION_VIEW)
            messageIntent.data = Uri.parse("sms:${contact.phoneNumber}")
            startActivity(messageIntent)
        }
    }
}