package com.luteh.contactapps.ui.listcontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luteh.contactapps.R
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsData

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class ListContactsAdapter(private val listener: (GetAllContactsData) -> Unit) :
    RecyclerView.Adapter<ListContactsHolder>() {

    private var dataSources: List<GetAllContactsData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListContactsHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_contacts_item, parent, false)

        return ListContactsHolder(itemView, listener)
    }

    override fun getItemCount(): Int = dataSources.size

    override fun onBindViewHolder(holder: ListContactsHolder, position: Int) {
        val data = dataSources[position]
        holder.bindTo(data)
    }

    fun setDataSources(allContactsData: List<GetAllContactsData>) {
        dataSources = allContactsData
        notifyDataSetChanged()
    }
}