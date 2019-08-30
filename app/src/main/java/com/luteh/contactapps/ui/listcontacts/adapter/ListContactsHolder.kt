package com.luteh.contactapps.ui.listcontacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsData
import kotlinx.android.synthetic.main.list_contacts_item.view.*

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class ListContactsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(getAllContactsData: GetAllContactsData) = with(itemView) {
        getAllContactsData.let {
            tv_list_contacts_name_item.text = "${it.firstName} ${it.lastName}"
            tv_list_contacts_age_item.text = "${it.age} years old"
        }
    }
}