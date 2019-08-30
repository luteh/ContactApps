package com.luteh.contactapps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luteh.contactapps.data.MyRepository
import com.luteh.contactapps.ui.listcontacts.ListContactsViewModel

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class MyViewModelFactory(private val myRepository: MyRepository) :
    ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListContactsViewModel::class.java) -> ListContactsViewModel(
                myRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}