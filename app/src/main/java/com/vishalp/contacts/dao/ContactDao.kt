package com.vishalp.contacts.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.vishalp.contacts.model.Contact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class ContactDao {
    private val TAG = "ContactDao"
    private val db = FirebaseFirestore.getInstance()
    val contactCollection = db.collection("Contact")

    fun addContact(contact: Contact?) {
        contact?.let {
            GlobalScope.launch() {
                contactCollection.document().set(it)
            }
        }
    }

    fun getContactById(contactId : String): Task<DocumentSnapshot> {
        return contactCollection.document(contactId).get()
    }

}