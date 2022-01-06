package com.vishalp.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreArray
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.vishalp.contacts.model.Contact

class RVAdaptor(options: FirestoreRecyclerOptions<Contact>,private val listener : IRVAdaptor) : FirestoreRecyclerAdapter<Contact,RVAdaptor.RVViewHolder>(
    options
) {

    class RVViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val phoneNumber : TextView = itemView.findViewById(R.id.phone)
        val call : ImageView = itemView.findViewById(R.id.call)
        val message : ImageView = itemView.findViewById(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val viewHolder = RVViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv,parent,false))
        viewHolder.call.setOnClickListener {
            listener.onCallClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.message.setOnClickListener {
            listener.onMessageClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: RVViewHolder, position: Int, model: Contact) {
        holder.name.text = model.firstName+" "+model.lastName
        holder.phoneNumber.text = model.phoneNumber
    }

}


interface IRVAdaptor {
    fun onCallClicked(contactId : String)
    fun onMessageClicked(contactId : String)
}